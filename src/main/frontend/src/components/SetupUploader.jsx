import React, {useEffect, useState} from "react";
import { FileUploader } from "react-drag-drop-files";
import Container from "react-bootstrap/Container";



const SetupUploader = (props) => {

    const fileTypes = ["ini"];

    const [file, setFile] = useState(null);
    const handleChange = (file) => {
        console.log('got file', file);
        setFile(file);
    };

    return (
      <Container>
          Setup Uploader
          <FileUploader handleChange={handleChange} name="file" types={fileTypes} />
      </Container>
    );
}


export default SetupUploader;