import React from 'react'
import { DataGrid } from '@mui/x-data-grid';
import Button from '@mui/material/Button';
import {Add} from '@mui/icons-material';


const pageSize = 5;
const columns = [
    { field: 'id', headerName: 'ID', width: 300 },
    { field: 'name', headerName: 'House name', width: 250 },
    { field: 'role', headerName: 'Role', width: 160,
        valueGetter: (params) => params.row.users.find( user => user.name==='Vlad Afrasinei').role
    },
    { field: 'usersNumber', headerName: 'Users', width: 140,
        valueGetter: (params) => params.row.users.length
    }, 
    { field: 'devicesNumber', headerName: 'Devices', width: 140,
        valueGetter: (params) => params.row.deviceIds.length 
    },
  ];

export default function Houses({houses, setHouses, setHouseSelected}) {
    return (
        <div className='component'>
            <div className='component-header'>
                <div className='component-header-addBtn'>
                    <Button 
                        variant="contained" 
                        color="success"
                        size="large"
                        startIcon={<Add />}>
                        New Home
                    </Button>
                </div>
            </div>

            <div className='component-dataGrid'>
                <DataGrid
                    rows={houses}
                    columns={columns}
                    autoHeight
                    pageSize={pageSize}
                    rowsPerPageOptions={[pageSize]}
                    onRowClick={(params)=> setHouseSelected(params.row)}
                    />
            </div>
        </div>
    )
}
