import React, {useEffect, useState} from "react";
import Container from 'react-bootstrap/Container';

import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import {Button} from "react-bootstrap";
import CarSelector from "./CarSelector";
import DifferenceTable from "./DifferenceTable";
import DataFetcher from "../service/DataFetcher";
import EventBus from "../common/EventBus";
import SetupUploader from "./SetupUploader";

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

    const [carTrackIniSelection, setCarTrackIniSelection]
        = useState(
        {allSelected: false}
    );

    const [showReScanButton, setShowReScanButton] = useState(false);

    const [differenceData, setDifferenceData] = useState(null);

    useEffect(() => {
        console.log("use effect reload-cars")

        EventBus.on("reload_cars", (data) => {

            console.log('Reloading cars from backend!');

            DataFetcher.getCarListForSelection(
                (response) => {
                    console.log('response', response);
                    EventBus.dispatch("notify_request", {
                        type: "INFO",
                        message: "Loaded " + response.data.length + " cars with setup ini files!",
                        title: "Info"
                    });
                    setCarList(response.data);
                },
                (err) => {
                    console.log("Network error: ", err);
                    EventBus.dispatch("notify_request", {
                        type: "ERROR",
                        message: err.message,
                        title: "Back End access"
                    });
                    setCarList(null);
                }
            );

        });


        return () => {
            EventBus.remove("reload_cars");
        };

    }, []);

    useEffect(() => {
        EventBus.on("compare_setups", (data) => {

            console.log('Comparing setups!', data);

            DataFetcher.sendCompareSetupsRequest(
                data,
                (response) => {
                    console.log('response', response);
                    setDifferenceData(response.data);
                },
                (err) => {
                    EventBus.dispatch("notify_request", {
                        type: "ERROR",
                        message: err.message,
                        title: "Back End access"
                    });
                }
            );

        });


        return () => {
            EventBus.remove("compare_setups");
        };

    }, []);

    useEffect(() => {
        console.log("use effect SetupIniFileStats")
        EventBus.on("scan_for_setup_ini_files", (data) => {
            DataFetcher.scanForSetupIniFiles(
                (response) => {
                    console.log("scan results", response);
                    setSetupIniFileStats(response.data);
                    localStorage.setItem("SetupIniFileStats", JSON.stringify(response.data))
                    EventBus.dispatch("reload_cars")
                },
                (err) => {
                    setSetupIniFileStats(null);
                    localStorage.removeItem("SetupIniFileStats");
                    EventBus.dispatch("notify_request", {
                        type: "ERROR",
                        message: err.message,
                        title: "Back End access"
                    });
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
            console.log("minus tmp", tmp);
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
        console.log("plus tmp", tmp);
        setIniSections(tmp);
        EventBus.dispatch("compare_setups", iniSections);

    }

    const carSelectCb = (data) => {
        console.log('callback -> ', data);
        setCarTrackIniSelection(data);
    }

    const handleReloadButton = (e) => {
        EventBus.dispatch("scan_for_setup_ini_files");
    }

    const handleButtonSet = (e, name) => {
        console.log("handleButtonSet: ", name);
        const tmp = [...iniSections];
        let section = tmp.find((section) => section.name === name);
        section.selected = carTrackIniSelection.selectedCar.concat(" / ").concat(carTrackIniSelection.selectedTrack).concat(" / ").concat(carTrackIniSelection.selectedSetupIni);
        console.log("section: ", section);
        setIniSections(tmp);
        EventBus.dispatch("compare_setups", iniSections);
    }

    return (
        <Container className='RootComponent-Container'>

            {
                showReScanButton === true
                    ?
                    <Row className='RootComponent-Reload-Row'>
                        <Col><Button onClick={handleReloadButton} variant="danger">Re-scan configs</Button></Col>
                    </Row>
                    :
                    <div></div>
            }

            {
                setupIniFileStats !== null
                    ?
                    <div className='RootComponent-Reload-Row'>
                        <Row>
                            <Col>scanDir</Col>
                            <Col>{setupIniFileStats.scanDir}</Col>
                        </Row>
                        <Row>
                            <Col>configKeyMapFile</Col>
                            <Col>{setupIniFileStats.configKeyMapFile}</Col>
                        </Row>
                        <Row>
                            <Col>carDirs</Col>
                            <Col>{setupIniFileStats.carDirs}</Col>
                        </Row>
                        <Row>
                            <Col>trackDirs</Col>
                            <Col>{setupIniFileStats.trackDirs}</Col>
                        </Row>
                        <Row>
                            <Col>uniqueSetupFiles</Col>
                            <Col>{setupIniFileStats.uniqueSetupFiles}</Col>
                        </Row>
                    </div>
                    :
                    <div>
                        <Row>
                            <Col>scanDir</Col>
                        </Row>
                        <Row>
                            <Col>configKeyMapFile</Col>
                        </Row>
                        <Row>
                            <Col>uniqueSetupFiles</Col>
                        </Row>
                        <Row>
                            <Col>carDirs</Col>
                        </Row>
                        <Row>
                            <Col>trackDirs</Col>
                        </Row>

                    </div>
            }
            <SetupUploader></SetupUploader>

            <CarSelector carList={carList} carSelectCb={carSelectCb}></CarSelector>

            {
                carTrackIniSelection !== null
                    ?
                    <Row className='RootComponent-Selection-Row'>
                        <Col>Selection:</Col>
                        <Col>{carTrackIniSelection.selectedCar}</Col>
                        <Col>{carTrackIniSelection.selectedTrack}</Col>
                        <Col>{carTrackIniSelection.selectedSetupIni}</Col>
                    </Row>
                    :
                    <></>
            }

            <Row>
                <Col><Button onClick={(e) => handleButtonMinus(e)}
                             className='RootComponent-ButtonMinus'>-</Button>
                    <Button onClick={handleButtonPlus}>+</Button>
                </Col>
            </Row>
            {
                iniSections.map(
                    (section, idx) => (
                        <Row key={idx}>
                            <Col><Button disabled={!carTrackIniSelection.allSelected} variant="primary"
                                         onClick={(e) => handleButtonSet(e, section.name)}>SET</Button> {section.name}
                            </Col>
                            <Col>{section.selected}</Col>
                        </Row>
                    )
                )
            }

            {
                differenceData !== null
                    ?
                    <Row>
                        <div><DifferenceTable data={differenceData.differences}
                                              iniSections={iniSections}></DifferenceTable></div>
                    </Row>
                    :
                    <Row>
                        <div>diff data null</div>
                    </Row>
            }

        </Container>
    );
}

export default RootComponent;