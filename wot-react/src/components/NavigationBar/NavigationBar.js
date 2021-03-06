import React, { useState } from 'react';
import './NavigationBar.scss'
import { useAuth } from "../../contexts/authcontext";
import { useLocation, useNavigate } from "react-router-dom";
import * as MuiIcons from '@mui/icons-material';
import Logo from '../../assets/logo';

import Profile from './../Profile/Profile.js'

const navigators = ['Dashboard', 'Profile', 'Help', 'Logout'];
const icons = ['Dashboard', 'Person', 'Help', 'Logout'];


const NavigationBar = () => {
    const [ navSelected, setNavSelected ] = useState(window.location.pathname.split('/')[1]);
    const { currentUser, logout } = useAuth();
    const location = useLocation()
    const navigate = useNavigate()


    const clickNavigator = (navigator) => {
        if( navigator === navSelected)
            return;
        if (navigator === 'Logout') {
            logout()
            setNavSelected(null)
            navigate('/login')
        }
        else if(navigator === 'Profile'){
            setNavSelected(navigator)

        }
        else { // Help, Dashboard
            setNavSelected(navigator)
            navigate(navigator)
        }

    }

    if (location?.pathname === '/login' || location?.pathname === '/register')
        return null;

    return (
        <>
            <div className='navBar'>
                <div className='navBar-logo'>
                    <Logo/>
                </div>
                <div className='navBar-account'>
                    <div className='navBar-account-img'>
                        <img src={require('../../assets/avatar_default.jpg')} alt="avatar_img"/>
                    </div>
                    <div className='navBar-account-name'>{currentUser?.username}</div>
                </div>
                <div className='navBar-navigators'>
                {
                    navigators.map((navigator,index) =>{
                        return(
                            <div
                                key={index}
                                onClick={() => clickNavigator(navigator)}
                                className={`navBar-navigators-item ${
                                    navigator===navSelected ? 'navBar-navigators-item--selected' :""}`}
                            >
                                {React.createElement(MuiIcons[icons[index]])} {navigator}
                            </div>
                        )
                    })
                }
                    
                </div>
            </div>
            <Profile
                isDialogOpen={navSelected==='Profile'}
                closeDialog={() => {setNavSelected('Dashboard'); navigate('Dashboard');}}
            />
        </>
    );
};
export default NavigationBar;
