import React, {useState} from "react";

import {Button, Dropdown} from "react-bootstrap";
import 'react-dropdown/style.css';
import DropdownItem from "react-bootstrap/DropdownItem";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import SetupDataFetcher from "../service/SetupDataFetcher";

const CarSelector = (props) => {

    const [selectedCar, setSelectedCar] = useState("-");
    const [selectedTrack, setSelectedTrack] = useState("-");
    const [trackList, setTrackList] = useState([]);
    const [iniList, setIniList] = useState([]);

    const RowTest = (props) => {
        const buttonPressed = (e) => {
            const id = e.clientX + '-' + e.clientY
            console.log('id -> : ' + id)
            console.log('buttonPressed: ', e)
            props.carSelectCb(id);
        };

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
            <Row>
                <Col>
                    <Dropdown onSelect={carSelected}>
                        <Dropdown.Toggle variant="success" id="dropdown-basic">
                            Select Car
                        </Dropdown.Toggle>
                        <Dropdown.Menu className='CarSelector_DropdownMenu'>
                            {
                                props.carList.map(
                                    (car) => (
                                        <DropdownItem
                                            eventKey={car.carFolderName}>{car.carFolderName}</DropdownItem>
                                    )
                                )
                            }
                        </Dropdown.Menu>
                    </Dropdown>
                </Col>
                <Col>{selectedCar}</Col>
                <Col>
                    {
                        selectedCar !== '-'
                            ?
                            <div>
                                <Dropdown onSelect={trackSelected}>
                                    <Dropdown.Toggle variant="success" id="dropdown-basic">
                                        Select Track
                                    </Dropdown.Toggle>
                                    <Dropdown.Menu>
                                        {
                                            trackList.map(
                                                (track) => (
                                                    <DropdownItem
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
                            select
                        </div>
                    }


                </Col>
            </Row>

        )
    }

    if (props.carList === null) {
        return <>Try to reload </>
    }

    return (
        <Row>
            <Col>
                <RowTest carList={props.carList} carSelectCb={props.carSelectCb}></RowTest>
            </Col>
        </Row>
    )
};

export default CarSelector;