import React, {useEffect, useState} from "react";
import {FileUploader} from "react-drag-drop-files";
import DataFetcher from "../service/DataFetcher";
import EventBus from "../common/EventBus";
import {Card} from "react-bootstrap";
import CardHeader from "react-bootstrap/CardHeader";


const SetupUploader = (props) => {

    const fileTypes = ["ini"];

    const [file, setFile] = useState(null);
    const [fileInfos, setFileInfos] = useState([]);

    useEffect(() => {
        DataFetcher.getFiles().then((response) => {
            console.log('getFiles response', response);
            setFileInfos(response.data);
        }).catch(
            (err) => {
                EventBus.dispatch("notify_request", {
                    type: "ERROR",
                    message: err.message,
                    title: "Back End access"
                });
            }
        );
    }, []);


    const handleChange = (file) => {
        console.log('got file', file);
        setFile(file);
    };

    return (
        <Card>
            <CardHeader>Setup Uploader</CardHeader>

            <FileUploader handleChange={handleChange} name="file" types={fileTypes}/>

            <Card>
                <CardHeader>List of uploaded Files</CardHeader>
                <ul className="list-group list-group-flush">
                    {fileInfos &&
                        fileInfos.map((file, index) => (
                            <li className="list-group-item" key={index}>
                                <a href={file.url}>{file.name}</a>
                            </li>
                        ))}
                </ul>
            </Card>
        </Card>


    );
}


export default SetupUploader;