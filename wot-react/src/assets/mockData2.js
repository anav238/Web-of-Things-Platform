export const houses_mock = [
    {
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "name": "Casa Bucium",
      "users": [
        {
          "userId": "f975d39a-212b-41dd-b694-9d50e5d139f3",
          "name": "Stefan Danila",
          "role": "Owner"
        },
        {
            "userId": "f51aa612-67c4-4b18-9a80-8c2f5f76df52",
            "name": "Vlad Afrasinei",
            "role": "Member"
          }
      ],
      "deviceIds": [
        "ba93d2a5-76f3-486d-b53c-62639a723dce",
        "bd5777e4-2f9b-459a-8671-51a840e183c7"
      ]
    },
    {
      "id": "eb7bb346-28b1-42be-bd14-b742e04ff4e3",
      "name": "Aprt Pacurari",
      "users": [
        {
          "userId": "f51aa612-67c4-4b18-9a80-8c2f5f76df52",
          "name": "Vlad Afrasinei",
          "role": "Owner"
        }
      ],
      "deviceIds": [
        "413e5393-75fb-4c00-8220-7a4edd1a0ec5"
      ]
    },
    {
      "id": "413e5393-75fb-4c00-8220-7a4edd1a0ec5",
      "name": "Aprt Pacurari Nord aka Barlogul",
      "users": [
        {
          "userId": "f51aa612-67c4-4b18-9a80-8c2f5f76df52",
          "name": "Vlad Afrasinei",
          "role": "Child"
        }
      ],
      "deviceIds": []
    }
      
  ]

  export const users_mock = [
    {
      "userId": "f975d39a-212b-41dd-b694-9d50e5d139f3",
      "name": "Stefan Danila",
      "homeIds": [
        "3fa85f64-5717-4562-b3fc-2c963f66afa6"
      ]
    },
    {
        "userId": "f51aa612-67c4-4b18-9a80-8c2f5f76df52",
        "name": "Vlad Afrasinei",
        "homeIds": [
            "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            "eb7bb346-28b1-42be-bd14-b742e04ff4e3"
          ]
      }
  ]

export  const devices_mock = [
    // first device
    {
        "id": "4ee812a6-c701-4324-b4b6-d46c0dee464f",
        "name": "string",
        "description": "string",
        "category": "Entertainment",
        "baseLink": "string",
        "properties": [
          {
            "type": "string",
            "title": "string",
            "valueType": "string",
            "currentValue": "string",
            "minimum": 0,
            "maximum": 0,
            "unit": "string",
            "description": "string",
            "links": [
              "string"
            ]
          }
        ],
        "actions": [
          {
            "title": "string",
            "description": "string",
            "input": {
              "type": "string",
              "required": [
                "string"
              ],
              "properties": [
                {
                  "type": "string",
                  "title": "string",
                  "valueType": "string",
                  "currentValue": "string",
                  "minimum": 0,
                  "maximum": 0,
                  "unit": "string",
                  "description": "string",
                  "links": [
                    "string"
                  ]
                }
              ]
            },
            "links": [
              "string"
            ]
          }
        ],
        "events": [
          "string"
        ],
        "links": [
          "string"
        ]
    },
    // second device
    {
        "id": "d711ab88-1222-45e2-b03f-7ed3f83a3120",
        "name": "string",
        "description": "string",
        "category": "Entertainment",
        "baseLink": "string",
        "properties": [
          {
            "type": "string",
            "title": "string",
            "valueType": "string",
            "currentValue": "string",
            "minimum": 0,
            "maximum": 0,
            "unit": "string",
            "description": "string",
            "links": [
              "string"
            ]
          }
        ],
        "actions": [
          {
            "title": "string",
            "description": "string",
            "input": {
              "type": "string",
              "required": [
                "string"
              ],
              "properties": [
                {
                  "type": "string",
                  "title": "string",
                  "valueType": "string",
                  "currentValue": "string",
                  "minimum": 0,
                  "maximum": 0,
                  "unit": "string",
                  "description": "string",
                  "links": [
                    "string"
                  ]
                }
              ]
            },
            "links": [
              "string"
            ]
          }
        ],
        "events": [
          "string"
        ],
        "links": [
          "string"
        ]
    },
    // third device
    {
        "id": "fad8fecb-892a-4f7e-96d9-42f41709cec5",
        "name": "string",
        "description": "string",
        "category": "Entertainment",
        "baseLink": "string",
        "properties": [
          {
            "type": "string",
            "title": "string",
            "valueType": "string",
            "currentValue": "string",
            "minimum": 0,
            "maximum": 0,
            "unit": "string",
            "description": "string",
            "links": [
              "string"
            ]
          }
        ],
        "actions": [
          {
            "title": "string",
            "description": "string",
            "input": {
              "type": "string",
              "required": [
                "string"
              ],
              "properties": [
                {
                  "type": "string",
                  "title": "string",
                  "valueType": "string",
                  "currentValue": "string",
                  "minimum": 0,
                  "maximum": 0,
                  "unit": "string",
                  "description": "string",
                  "links": [
                    "string"
                  ]
                }
              ]
            },
            "links": [
              "string"
            ]
          }
        ],
        "events": [
          "string"
        ],
        "links": [
          "string"
        ]
    }  
  ]
