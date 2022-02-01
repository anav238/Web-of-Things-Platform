import React, {useState} from 'react'
import HouseDialog from "./HouseDialog"
import { DataGrid } from '@mui/x-data-grid';
import { styled } from '@mui/material/styles';
import Button from '@mui/material/Button';
import { Edit } from '@mui/icons-material';


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
    const [isDialogOpen, setIsDialogOpen] = useState(false)
    const [formData, setFormData] = useState(emptyFormData);


    const submitForm = () => {
        console.log(formData);
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
                <div className='component-header-editBtn'>
                    <ColorOutlinedButton 
                        size="large"
                        onClick={onEditUsers}
                        startIcon={<Edit />}
                    >
                            Add Edit users
                    </ColorOutlinedButton>
                </div>  
            </div>

            <div className='component-dataGrid'>
                <DataGrid
                    rows={houseSelected.users.map((user, index)=> ({...user, id: index}))}
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
