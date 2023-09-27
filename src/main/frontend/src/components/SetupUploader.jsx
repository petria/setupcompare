import React, {useEffect, useState} from "react";
import {FileUploader} from "react-drag-drop-files";
import Container from "react-bootstrap/Container";
import DataFetcher from "../service/DataFetcher";
import EventBus from "../common/EventBus";


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
        <Container>
            Setup Uploader
            <FileUploader handleChange={handleChange} name="file" types={fileTypes}/>

            <div className="card">
                <div className="card-header">List of Files</div>
                <ul className="list-group list-group-flush">
                    {fileInfos &&
                        fileInfos.map((file, index) => (
                            <li className="list-group-item" key={index}>
                                <a href={file.url}>{file.name}</a>
                            </li>
                        ))}
                </ul>
            </div>

        </Container>
    );
}


export default SetupUploader;