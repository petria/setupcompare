import React, {useEffect, useState} from "react";
import Container from 'react-bootstrap/Container';

import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import {Button} from "react-bootstrap";
import CarSelector from "./CarSelector";

import SetupDataFetcher from "../service/SetupDataFetcher";
import EventBus from "../common/EventBus";

const RootComponent = () => {

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

    const [setupIniFileStats, setSetupIniFileStats] = useState(null);

    const [carList, setCarList] = useState(null);

    useEffect(() => {
        console.log("use effect reload-cars")

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

        return () => {
            EventBus.remove("reload_cars");
        };

    }, []);

    useEffect(() => {
        console.log("use effect SetupIniFileStats")
        EventBus.on("scan_for_setup_ini_files", (data) => {
            SetupDataFetcher.scanForSetupIniFiles(
                (response) => {
                    console.log("scan results", response);
                    setSetupIniFileStats(response.data);
                    localStorage.setItem("SetupIniFileStats", JSON.stringify(response.data))
                    EventBus.dispatch("reload_cars")
                },
                (err) => {
                    EventBus.dispatch("notify_test", {type: "ERROR", message: err.message, title: "Back End access"});
                }
            );
        });

        const localData = localStorage.getItem("SetupIniFileStats");
        if (localData === null) {
            console.log("send initial scan request!");
            EventBus.dispatch("scan_for_setup_ini_files")
        } else {
            setSetupIniFileStats(JSON.parse(localStorage.getItem("SetupIniFileStats")));
            console.log("Got scan stats from localStorage!");
            EventBus.dispatch("reload_cars")
        }

        return () => {
            EventBus.remove("scan_for_setup_ini_files");
        };

    }, []);




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
//        console.log('handle reload ->', e);
        EventBus.dispatch("scan_for_setup_ini_files");
    }

    return (
        <Container className='RootComponent-Container'>

            <Row className='RootComponent-Reload-Row'>
                <Col><Button onClick={handleReloadButton} variant="danger">Re-scan configs</Button></Col>
            </Row>

            {
                setSetupIniFileStats === null
                    ?
                    <Row>
                        <div>null</div>
                    </Row>
                    :
                    <Row>
                        <div>not null</div>
                    </Row>
            }

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