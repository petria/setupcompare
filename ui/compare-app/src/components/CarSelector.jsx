import React, {useEffect, useState} from "react";

import {Button, Dropdown} from "react-bootstrap";
import 'react-dropdown/style.css';
import DropdownItem from "react-bootstrap/DropdownItem";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import SetupDataFetcher from "../service/SetupDataFetcher";

const CarSelector = (props) => {

    const [selectedCar, setSelectedCar] = useState("-");
    const [selectCarText, setSelectCarText] = useState("Select Car")

    const [selectedTrack, setSelectedTrack] = useState("-");
    const [selectTrackText, setSelectTrackText] = useState("Select Track")

    const [trackList, setTrackList] = useState([]);
    const [iniList, setIniList] = useState([]);

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

    const CarSelectorRow = (props) => {

        const carSelected = (eventKey, event) => {

            setSelectedCar(eventKey);
            SetupDataFetcher.getTrackListForCar(
                eventKey,
                (response) => {
                    console.log('response', response);
                    setTrackList(response.data.trackList);
                    setSelectedTrack('-');
                },
                (error) => {
                    console.log('error', error); // TODO
                }
            );
        }

        const trackSelected = (eventKey, event) => {
            setSelectedTrack(eventKey);
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
                                                                  eventKey={car.carFolderName}>{car.carFolderName}</DropdownItem>
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
                            select ini
                        </div>
                    }


                </Col>
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