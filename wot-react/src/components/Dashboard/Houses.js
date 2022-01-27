import React, {useState} from 'react'
import HouseDialog from "./HouseDialog"
import { DataGrid } from '@mui/x-data-grid';
import { styled } from '@mui/material/styles';
import Button from '@mui/material/Button';
import {Add, Edit} from '@mui/icons-material';


const pageSize = 5;
const columns = [
    { field: 'id', headerName: 'ID', width: 300 },
    { field: 'name', headerName: 'House name', width: 250 },
    { field: 'role', headerName: 'Role', width: 160,
        valueGetter: (params) => params.row.users.find( user => user.name==='Vlad Afrasinei')?.role
    },
    { field: 'usersNumber', headerName: 'Users', width: 140,
        valueGetter: (params) => params.row.users.length
    }, 
    { field: 'devicesNumber', headerName: 'Devices', width: 140,
        valueGetter: (params) => params.row.deviceIds.length 
    },
  ];

const ColorButton = styled(Button)(({ theme }) => ({
color: theme.palette.getContrastText('rgba(0, 123, 85, 0.5)'),
backgroundColor: 'rgba(0, 123, 85, 0.7)',
'&:hover': {
    backgroundColor: 'rgba(0, 123, 85, 0.9)',
},
}));
const ColorOutlinedButton = styled(Button)(({ theme }) => ({
padding: '6px 22px',
fontWeight: 600,
color: 'rgba(0, 123, 85, 0.5)',
border: '2px solid',
borderColor: 'rgba(0, 123, 85, 0.5)',
backgroundColor: 'rgba(0, 0, 0 0.0)',
'&:hover': {
    backgroundColor: 'rgba(0, 123, 85, 0.1)',
    borderColor: 'rgba(0, 123, 85, 0.5)',
},
}));

const emptyFormData = {name: '', users: [{name:'', role: ''}]};

export default function Houses({houses, setHouses, houseSelected, setHouseSelected}) {
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const [inEditMode, setInEditMode] = useState(false);
    const [formData, setFormData] = useState(JSON.parse(JSON.stringify(emptyFormData)));

    const submitForm = () => {
        console.log(formData);
        //console.log(houseSelected);

        //setIsDialogOpen(false);
        //setHouses([...houses,  apiResponse(formData)]);
    }

    const onAddHome = () => {
        setInEditMode(false);
        setIsDialogOpen(true);
        setFormData(JSON.parse(JSON.stringify(emptyFormData)));
    }

    const onEditHome = () => {
        setInEditMode(true);
        setIsDialogOpen(true);
        setFormData(JSON.parse(JSON.stringify(houseSelected)));
    }


    return (
        <div className='component'>
            <div className='component-header'>
                <div className='component-header-addBtn'>
                    <ColorButton 
                        variant="contained" 
                        size="large"
                        onClick={onAddHome}
                        startIcon={<Add />}>
                        New Home
                    </ColorButton>
                </div>
                <div className='component-header-editBtn'>
                    <ColorOutlinedButton 
                        size="large"
                        onClick={onEditHome}
                        startIcon={<Edit />}
                        disabled={houseSelected ? false : true}
                    >
                            Edit Home
                    </ColorOutlinedButton>
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

            <HouseDialog 
                isDialogOpen={isDialogOpen}
                setIsDialogOpen={setIsDialogOpen}
                formData={formData}
                setFormData={setFormData}
                submitForm={submitForm}
                isEdit={inEditMode}
            />
        </div>
    )
}
