import React from "react";

const DifferenceTable = (props) => {

    const getRowColumns = (row) => {
        const cols = [];
        row.differences.forEach(
            (diff) => cols.push(
                <td>{diff}</td>
            )
        );
        return cols;
    }

    const tableRows = props.data.map(
        (row, idx) =>
            <tr key={idx}>
                {getRowColumns(row)}
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