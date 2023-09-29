import React, {useEffect, useState} from "react";

import {Button, Dropdown} from "react-bootstrap";
import 'react-dropdown/style.css';
import DropdownItem from "react-bootstrap/DropdownItem";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import DataFetcher from "../service/DataFetcher";

const CarSelector = (props) => {

    const [selectedCar, setSelectedCar] = useState("-");
    const [selectCarText, setSelectCarText] = useState("Select Car")

    const [trackList, setTrackList] = useState([]);
    const [selectedTrack, setSelectedTrack] = useState("-");
    const [selectTrackText, setSelectTrackText] = useState("Select Track")

    const [setupIniList, setSetupIniList] = useState([]);
    const [selectedSetupIni, setSelectedSetupIni] = useState("-");
    const [selectSetupIniText, setSelectSetupIniText] = useState("Select setup ini")

    useEffect(() => {
        if (props.carList) {
            const text = "Select car (" + props.carList.length + ")";
            setSelectCarText(text);
        }
    }, [props.carList]);

    useEffect(() => {
        if (trackList !== null) {
            const text = "Select track (" + trackList.length + ")";
            setSelectTrackText(text);
        }
    }, [trackList]);

    useEffect(() => {
        if (setupIniList !== null) {
            const text = "Select setup ini (" + setupIniList.length + ")";
            setSelectSetupIniText(text);
        }
    }, [setupIniList]);

    useEffect(() => {
        const allSelected = selectedCar !== '-' && selectedTrack !== '-' && selectedSetupIni !== '-';
        props.carSelectCb(
            {
                allSelected: allSelected,
                selectedCar: selectedCar,
                selectedTrack: selectedTrack,
                selectedSetupIni: selectedSetupIni
            }
        )
    }, [selectedCar, selectedTrack, selectedSetupIni]);

    const CarSelectorRow = (props) => {

        const carSelected = (eventKey, event) => {

            setSelectedCar(eventKey);
            DataFetcher.getTrackListForCar(
                eventKey,
                (response) => {
                    console.log('response', response);
                    setTrackList(response.data.trackList);
                    setSelectedTrack('-');
                    setSelectedSetupIni('-')
                },
                (error) => {
                    console.log('error', error); // TODO
                }
            );
        }

        const trackSelected = (eventKey, event) => {
            setSelectedTrack(eventKey);
            DataFetcher.getSetupListForCarAndTrack(
                selectedCar,
                eventKey,
                (response) => {
                    console.log('response', response);
                    setSetupIniList(response.data.setupList);
                },
                (error) => {
                    setSetupIniList(null);
                    console.log('error', error); // TODO
                }
            );
        }

        const setupIniSelected = (eventKey, event) => {
            setSelectedSetupIni(eventKey);
            console.log('setupIniSelected', eventKey);
        }

        function formatCarDropDown(car) {
            const str = car.carFolderName.concat(' (').concat(car.carTracksWithSetup).concat(')');
            return str;
        }

        return (
            <Row className='CarSelector_Row'>
                <Col>
                    {
                        props.carList !== null
                            ? <div>
                                <Dropdown onSelect={carSelected}>
                                    <Dropdown.Toggle variant="success" id="dropdown-basic">
                                        {selectCarText}
                                    </Dropdown.Toggle>
                                    <Dropdown.Menu className='CarSelector_DropdownMenu'>
                                        {
                                            props.carList.map(
                                                (car) => (
                                                    <DropdownItem key={car.carFolderName}
                                                                  eventKey={car.carFolderName}>{formatCarDropDown(car)}</DropdownItem>
                                                )
                                            )
                                        }
                                    </Dropdown.Menu>
                                </Dropdown>
                            </div>
                            :
                            <div>
                                <Button variant="danger">Re-scan configs</Button>
                            </div>
                    }
                </Col>
                <Col>{selectedCar}</Col>
                <Col>
                    {
                        selectedCar !== '-'
                            ?
                            <div>
                                <Dropdown onSelect={trackSelected}>
                                    <Dropdown.Toggle variant="success" id="dropdown-basic">
                                        {selectTrackText}
                                    </Dropdown.Toggle>
                                    <Dropdown.Menu>
                                        {
                                            trackList.map(
                                                (track) => (
                                                    <DropdownItem key={track.trackFolderName}
                                                                  eventKey={track.trackFolderName}>{track.trackFolderName}</DropdownItem>
                                                )
                                            )
                                        }
                                    </Dropdown.Menu>
                                </Dropdown>
                            </div>
                            :
                            <div>
                                <Button variant="danger">Select car first</Button>
                            </div>
                    }
                </Col>
                <Col>{selectedTrack}</Col>
                <Col>
                    {selectedTrack === '-'
                        ?
                        <div>
                            <Button variant="danger">Select car&track first</Button>
                        </div>
                        :
                        <div>
                            <Dropdown onSelect={setupIniSelected}>
                                <Dropdown.Toggle variant="success" id="dropdown-basic">
                                    {selectSetupIniText}
                                </Dropdown.Toggle>
                                <Dropdown.Menu>
                                    {
                                        setupIniList.map(
                                            (setupIni) => (
                                                <DropdownItem key={setupIni.id}
                                                              eventKey={setupIni.setupIniFileName}>{setupIni.setupIniFileName}</DropdownItem>
                                            )
                                        )
                                    }
                                </Dropdown.Menu>
                            </Dropdown>

                        </div>
                    }
                </Col>
                <Col>{selectedSetupIni}</Col>

            </Row>

        )
    }

    return (
        <Row>
            <Col>
                <CarSelectorRow carList={props.carList} carSelectCb={props.carSelectCb}></CarSelectorRow>
            </Col>
        </Row>
    );
};

export default CarSelector;