package main

import (
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
	"os/exec"
	"runtime"
	"encoding/json"
)

type LatestData struct {
	Release string `json:"release"`
}

type Data struct {
	Latest LatestData `json:"latest"`
	Current string `json:"current"`
}

func main() {
	var latest string;
	var current string;

	fileContent, err := ioutil.ReadFile("version.dat")

	var fileData Data
	err = json.Unmarshal(fileContent, &fileData)
	current = fileData.Current

	resp, err := http.Get("https://f.techman.dev/d/andromeda/version.json")
	if err != nil {
		fmt.Println("Error:", err)
		return
	}
	defer resp.Body.Close()

	body,err := ioutil.ReadAll(resp.Body)

	var onlineData Data
	err = json.Unmarshal(body, &onlineData)
	latest = onlineData.Latest.Release

	if (current != latest) {
		update(latest)
	}

	var command string

	if runtime.GOOS == "windows" {
		command = "./Andromeda.exe"
	} else {
		command = "./Andromeda"
	}

	cmd := exec.Command(command)
	cmd.Start()
	os.Exit(0)
}


func update(newVersion string) {

	var url string;

	if runtime.GOOS == "windows" {
		url = "https://credo-downloads.s3.us-east-2.amazonaws.com/public/v1.011/Credo+Calculator+Installer.exe"
	} else if runtime.GOOS == "linux" {
		url = ""
	} else if runtime.GOOS == "darwin" {
		url = ""
	} else {
		return
	}

	resp, err := http.Get(url)
	if err != nil {
		fmt.Println("Error:", err)
		return
	}
	defer resp.Body.Close()

	// Read response body into a variable
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		fmt.Println("Error:", err)
		return
	}

	// Write the response body to a file
	var fileName string

	if runtime.GOOS == "windows" {
		fileName = "Andromeda.exe"
	} else {
		fileName = "Andromeda"
	}

	file, err := os.Create(fileName)
	if err != nil {
		fmt.Println("Error:", err)
		return
	}
	defer file.Close()

	_, err = file.Write(body)
	if err != nil {
		fmt.Println("Error:", err)
		return
	}

	ioutil.WriteFile("version.dat", []byte("{\"current\":\""+newVersion+"\"}"), 0644)
}