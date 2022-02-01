import React, {useState} from 'react'
import HouseDialog from "./HouseDialog"
import { DataGrid } from '@mui/x-data-grid';
import { styled } from '@mui/material/styles';
import Button from '@mui/material/Button';
import {Add, Edit} from '@mui/icons-material';
import { useAuth } from "../../contexts/authcontext";
import axios from 'axios'

const pageSize = 5;

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

const emptyFormData = {name: '', users: []};

export default function Houses({houses, setHouses, houseSelected, setHouseSelected}) {
    
    const { currentUser } = useAuth();
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const [inEditMode, setInEditMode] = useState(false);
    const [formData, setFormData] = useState(JSON.parse(JSON.stringify(emptyFormData)));

    const columns = [
        { field: 'id', headerName: 'ID', width: 300 },
        { field: 'name', headerName: 'House name', width: 250 },
        { field: 'role', headerName: 'Role', width: 160,
            valueGetter: (params) => params.row.users.find( user => user.userId===currentUser?.id)?.userRole
        },
        { field: 'usersNumber', headerName: 'Users', width: 140,
            valueGetter: (params) => params.row.users.length
        }, 
        { field: 'devicesNumber', headerName: 'Devices', width: 140,
            valueGetter: (params) => params.row.deviceIds.length 
        },
      ];

    

    const addHome = async () => {
        const data = JSON.parse(JSON.stringify(formData));
        data.users = [...data.users, {userId: currentUser.id, userRole: 'OWNER'}]

        await axios.post(`/homes`,data)
        .then(response => { 
            setHouses([...houses,  response.data]);
        })
        .catch(error => {
            console.log(error?.response);
        });
    }

    const editHome = async () => {
        const house = JSON.parse(JSON.stringify(formData));

        const payload = [
            {
                op: "add",
                path: "/users",
                value: house.users
            },
            {
                op: "add",
                path: "/name",
                value: house.name
            }
        ];

        await axios.patch(`/homes/${houseSelected.id}`,payload,{
            headers: {
                'Content-Type': 'application/json-patch+json',
            }
        })
        .then(response => { 
            const newHouses = JSON.parse(JSON.stringify(houses));
            const index = newHouses.findIndex( h => h.id===response.data.id)
            if (index !== -1) {
                newHouses[index] = response.data;
            }
            setHouseSelected(response.data);
            setHouses(newHouses);
        })
        .catch(error => {
            console.log(error?.response);
        });
    }

    const deleteHome = () => {
        axios.delete(`/homes/${houseSelected.id}`).then(() => { 
            const newHouses = JSON.parse(JSON.stringify(houses));
            const index = newHouses.findIndex( h => h.id === houseSelected.id)
            if (index !== -1) {
                newHouses.splice(index, 1);
            }
            setHouseSelected(null);
            setHouses(newHouses);
            setIsDialogOpen(false);
        })
        .catch(error => {
            console.log(error?.response);
        });
    }
    
    const submitForm = () => {

        if(inEditMode){
            editHome();
        }
        else{
            addHome();
        }
        setIsDialogOpen(false);
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
                {
                    (!houseSelected || houseSelected.users.find( user => user.userId===currentUser?.id)?.userRole === 'OWNER') ?
                    
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
                    :
                    <></>  
                }
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
                deleteHome={deleteHome}
            />
        </div>
    )
}
