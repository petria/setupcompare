import React, {useEffect, useState} from "react";
import Container from 'react-bootstrap/Container';

import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import {Button} from "react-bootstrap";
import CarSelector from "./CarSelector";

import SetupDataFetcher from "../service/SetupDataFetcher";
import EventBus from "../common/EventBus";

const RootComponent = () => {


    const [carList, setCarList] = useState(null);

    useEffect(() => {
        console.log("use effect!")

        EventBus.on("reload_cars", (data) => {

            console.log('Reloading cars from backend!');

            SetupDataFetcher.getCarListForSelection(
                (response) => {
                    console.log('response', response);
                    EventBus.dispatch("notify_test", {
                        type: "INFO",
                        message: "Loaded " + response.data.length + " cars!",
                        title: "Info"
                    });
                    setCarList(response.data);
                },
                (err) => {
                    console.log("Network error: ", err);
                    EventBus.dispatch("notify_test", {type: "ERROR", message: err.message, title: "Back End access"});
                    setCarList(null);
                }
            );

        });

        EventBus.dispatch("reload_cars")

        return () => {
            EventBus.remove("reload_cars");
        };

    }, []);


    const [iniSections, setIniSections] = useState([
        {
            "name": "Base Setup",
            "selected": "-"

        },
        {
            "name": "Setup #1",
            "selected": "-"
        }
    ]);


    const handleButtonMinus = (e) => {
        console.log("Minus", iniSections.length);
        if (iniSections.length > 2) {
            //          console.log("iniSections", iniSections);
            const tmp = [...iniSections];
            tmp.pop();
            console.log("tmp", tmp);
            setIniSections(tmp);
        }
    }

    const handleButtonPlus = () => {
        const num = iniSections.length;
        const section = {
            "name": "Setup #".concat(num),
            "selected": "-"
        }
        const tmp = [...iniSections];
        tmp.push(section);
        setIniSections(tmp);
    }

    const carSelectCb = (data) => {
        console.log('callback -> ', data);
//        setCallBackData(data);
    }

    const handleReloadButton = (e) => {
        console.log('handle reload ->', e);
        EventBus.dispatch("reload_cars");
//        EventBus.dispatch("notify_test", {type: "ERROR", message: "Sending reload request!", title: "fyi"});

//        createNotification('Sending reload request!');
    }

    return (
        <Container className='RootComponent-Container'>

            <Row className='RootComponent-Reload-Row'>
                <Col><Button onClick={handleReloadButton} variant="danger">Re-scan configs</Button></Col>
            </Row>


            <CarSelector carList={carList} carSelectCb={carSelectCb}></CarSelector>

            <Row>
                <Col><Button onClick={(e) => handleButtonMinus(e)}
                             className='RootComponent-ButtonMinus'>-</Button><Button
                    onClick={handleButtonPlus}>+</Button></Col>
            </Row>

            {
                iniSections.map(
                    (section, idx) => (
                        <Row key={idx}>
                            <Col>{section.name}</Col>
                            <Col>{section.selected}</Col>

                        </Row>
                    )
                )
            }

            <Row className='RootComponent-Row'>

            </Row>

            <Row className='RootComponent-Row'>
                {
                    iniSections.map(
                        (section, idx) => (
                            <Col key={idx}>{section.name}</Col>
                        )
                    )
                }
            </Row>

            <Row className='RootComponent-Row'>
                <Col className='RootComponent-Col'>OneCOL</Col>
            </Row>
        </Container>
    );
}

export default RootComponent;