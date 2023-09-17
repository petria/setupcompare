import React from "react";

const DifferenceTable = (props) => {

    const tableRows = props.data.map(
        (row, idx) =>
            <tr key={idx}>
                <td>{row.differences[0]}</td>
                <td>{row.differences[1]}</td>
                <td>{row.differences[2]}</td>
                <td>{row.differences[3]}</td>
            </tr>
    );

    const headers = ['Section', 'Setting'];
    props.iniSections.forEach(
        (section) => {
            headers.push(section.name);
        }
    );

    const tableHeaders = headers.map((header, idx) =>
        <th>{header}</th>
    );

//    console.log('DifferenceTable ->', props.data);

    return (
        <>
            <table className="table">
                <thead>
                <tr>
                    {tableHeaders}
                </tr>
                </thead>
                <tbody>
                {tableRows}
                </tbody>
            </table>
        </>

    );
}

export default DifferenceTable;