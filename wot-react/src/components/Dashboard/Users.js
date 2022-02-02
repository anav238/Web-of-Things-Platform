import React, {useState, useEffect} from 'react'
import HouseDialog from "./HouseDialog"
import { DataGrid } from '@mui/x-data-grid';
import { styled } from '@mui/material/styles';
import Button from '@mui/material/Button';
import { Edit } from '@mui/icons-material';
import { useAuth } from "../../contexts/authcontext";
import axios from 'axios';


const pageSize = 5;
const columns = [
    { field: 'id', headerName: 'No.', width: 40 },
    { field: 'userId', headerName: 'ID', width: 300 },
    { field: 'name', headerName: 'User name', width: 250 },
    { field: 'userRole', headerName: 'Role', width: 160 },
  ];

// const ColorButton = styled(Button)(({ theme }) => ({
// color: theme.palette.getContrastText('rgba(12, 83, 183, 0.5)'),
// backgroundColor: 'rgba(12, 83, 183, 0.7)',
// '&:hover': {
//     backgroundColor: 'rgba(12, 83, 183, 0.9)',
// },
// }));
const ColorOutlinedButton = styled(Button)(({ theme }) => ({
padding: '6px 22px',
fontWeight: 600,
color: 'rgba(12, 83, 183, 0.5)',
border: '2px solid',
borderColor: 'rgba(12, 83, 183, 0.5)',
backgroundColor: 'rgba(0, 0, 0 0.0)',
'&:hover': {
    backgroundColor: 'rgba(12, 83, 183, 0.1)',
    borderColor: 'rgba(12, 83, 183, 0.5)',
},
}));

const emptyFormData = {name: '', users: [{name:'', role: ''}]};


export default function Users({houseSelected}) {
    const { currentUser } = useAuth();
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const [formData, setFormData] = useState(emptyFormData);
    const [rowsData, setRowsData] = useState(JSON.parse(JSON.stringify(houseSelected.users)));


    useEffect(() => {

        const axiosReq = []
        const newRowsData = JSON.parse(JSON.stringify(houseSelected.users))
        houseSelected.users.map(user => currentUser.id === user.userId 
            ?
                newRowsData[newRowsData.findIndex( user => user.userId===currentUser.id)].name = currentUser.username
            :
                axiosReq.push( axios.get(`/users/${user.userId}`)
            )
        )

        axios.all(axiosReq).then(axios.spread((...responses) => {
                responses.map( (response) => (
                    newRowsData[newRowsData.findIndex( user => user.userId===response.data.id)].name = response.data.username
                ))
                setRowsData([...newRowsData,]);
            }))
            .catch(errors => {
                console.log(errors?.data);
              })

    }, [houseSelected, currentUser])

    const submitForm = () => {
        //console.log(houseSelected);

        //setIsDialogOpen(false);
        //setHouses([...houses, apiResponse(formData)]);
    }

    const onEditUsers = () => {
        setIsDialogOpen(true);
        setFormData(JSON.parse(JSON.stringify(houseSelected)));
    }

    return (
        <div className='component'>
            <div className='component-header'>
            {
                (!houseSelected || houseSelected.users.find( user => user.userId===currentUser?.id)?.userRole === 'OWNER')
                    &&
                <div className='component-header-editBtn'>
                    <ColorOutlinedButton 
                        size="large"
                        onClick={onEditUsers}
                        startIcon={<Edit />}
                    >
                            Add Edit users
                    </ColorOutlinedButton>
                </div> 
            }  
            </div>

            <div className='component-dataGrid'>
                <DataGrid
                    rows={rowsData.map((user, index)=> ({...user, id: index}))}
                    columns={columns}
                    autoHeight
                    pageSize={pageSize}
                    rowsPerPageOptions={[pageSize]}
                    // onRowClick={(params)=> setHouseSelected(params.row)}
                    />
            </div>

            <HouseDialog 
                isDialogOpen={isDialogOpen}
                setIsDialogOpen={setIsDialogOpen}
                formData={formData}
                setFormData={setFormData}
                submitForm={submitForm}
                isEdit={true}
            />
        </div>
    )
}
