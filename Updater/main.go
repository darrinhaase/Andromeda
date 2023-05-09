package main

import (
	"encoding/json"
	"fmt"
	"io"
	"io/ioutil"
	"net/http"
	"os"
	"os/exec"
	"runtime"
	"github.com/therecipe/qt/core"
	"github.com/therecipe/qt/widgets"
)

type LatestData struct {
	Release string `json:"release"`
}

type Data struct {
	Latest  LatestData `json:"latest"`
	Current string     `json:"current"`
}

func main() {
	app := widgets.NewQApplication(len(os.Args), os.Args)

	window := widgets.NewQMainWindow(nil, 0)
	window.SetWindowTitle("Loading Example")
	window.SetMinimumSize2(300, 200)

	centralWidget := widgets.NewQWidget(nil, 0)
	centralWidget.SetLayout(widgets.NewQVBoxLayout())

	loadingText := widgets.NewQLabel2("Updating...", nil, 0)

	centralWidget.Layout().AddWidget(loadingText)

	window.SetCentralWidget(centralWidget)
	window.Show()

	// Simulate a loading process
	time.Sleep(3 * time.Second)

	// Update the text and hide the loading icon
	loadingText.SetText("Update Complete")
	loadingIcon.Hide()

	app.Exec()

	var latest string
	var current string

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

	body, err := ioutil.ReadAll(resp.Body)

	var onlineData Data
	err = json.Unmarshal(body, &onlineData)
	latest = onlineData.Latest.Release

	if current != latest {
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

	var url string
	var fileName string

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

	_, err = io.Copy(file, resp.Body)
	if err != nil {
		fmt.Println("Error writing to the file:", err)
		return
	}

	if runtime.GOOS != "windows" {
		err = os.Chmod("Andromeda", 0755)
		if err != nil {
			fmt.Println("Error setting file permissions:", err)
			return
		}
	}

	ioutil.WriteFile("version.dat", []byte("{\"current\":\""+newVersion+"\"}"), 0644)
}
