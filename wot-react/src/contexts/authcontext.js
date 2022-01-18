import axios from 'axios'
import React, { useContext, useState, useEffect } from 'react'

const AuthContext = React.createContext(undefined)

export function useAuth() {
    return useContext(AuthContext)
}

export function AuthProvider({children}) {
    const initialUser = () => JSON.parse(window.localStorage.getItem("currentUser"));
    const [currentUser, setCurrentUser] = useState(initialUser)

    function signup(username, email, password){
        const data = {
            username: username,
            password: password,
            email: email
        }

        return axios.post('http://localhost:5005/api/Users', data).catch(err =>{
            return err.response.status
        })
    }

    function login(username, password){
        const data = {
            username: username,
            password: password
        }
        setCurrentUser(data)
        
        // return axios.post('http://localhost:5005/api/Users/authenticate', data).then(
        //     res => {
        //         setCurrentUser(res.data)
        //         return res.status;
        //     }
        // ).catch(err =>{
        //     return err.response.data.status
        // })
    }

    function logout(){
        setCurrentUser(null)
    }


    useEffect(() =>{
        window.localStorage.setItem('currentUser', JSON.stringify(currentUser))
    },[currentUser])

    const value = {
        currentUser,
        signup,
        login,
        logout
    }
    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    )
}

export default AuthContext
