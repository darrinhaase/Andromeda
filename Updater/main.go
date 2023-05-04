package main

import (
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
	"runtime"
)

func main() {

	var url string;

	if runtime.GOOS == "windows" {
		url = ""
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
	file, err := os.Create("out.exe")
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
}
