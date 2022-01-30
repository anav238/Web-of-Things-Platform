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
        "851de9de-e6a6-4817-ac12-68a19f875e51"
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
    {
      "uri": "/devices/851de9de-e6a6-4817-ac12-68a19f875e51",
      "id": "851de9de-e6a6-4817-ac12-68a19f875e51",
      "title": "My Lamp",
      "description": "A web connected lamp",
      "category": null,
      "baseLink": null,
      "properties": [
          {
            "type": "boolean",
            "name": "Power",
            "title": null,
            "currentValue": 'On',
            "minimum": null,
            "maximum": null,
            "unit": null,
            "description": "Whether the lamp is turned on"
        },
        {
            "type": "integer",
            "name": "Brightness",
            "title": null,
            "currentValue": 40,
            "minimum": null,
            "maximum": null,
            "unit": "percent",
            "description": "The level of light from 0-100"
        }
      ],
      "actions": [
          {
              "name": "Fade",
              "title": null,
              "description": "Fade the lamp to a given level",
              "input": {
                  "type": "object",
                  "required": [
                      "brightness",
                      "duration"
                  ],
                  "properties": [
                      {
                          "type": "integer",
                          "name": null,
                          "title": null,
                          "currentValue": null,
                          "minimum": null,
                          "maximum": null,
                          "unit": "percent",
                          "description": null
                      },
                      {
                          "type": "integer",
                          "name": null,
                          "title": null,
                          "currentValue": null,
                          "minimum": null,
                          "maximum": null,
                          "unit": "milliseconds",
                          "description": null
                      }
                  ]
              }
          },
          {
            "name": "Fade",
            "title": null,
            "description": "Fade the lamp to a given level",
            "input": {
                "type": "object",
                "required": [
                    "brightness",
                    "duration"
                ],
                "properties": [
                    {
                        "type": "integer",
                        "name": null,
                        "title": null,
                        "currentValue": null,
                        "minimum": null,
                        "maximum": null,
                        "unit": "percent",
                        "description": null
                    },
                    {
                        "type": "integer",
                        "name": null,
                        "title": null,
                        "currentValue": null,
                        "minimum": null,
                        "maximum": null,
                        "unit": "milliseconds",
                        "description": null
                    }
                ]
            }
        },
        {
          "name": "Fade",
          "title": null,
          "description": "Fade the lamp to a given level",
          "input": {
              "type": "object",
              "required": [
                  "brightness",
                  "duration"
              ],
              "properties": [
                  {
                      "type": "integer",
                      "name": null,
                      "title": null,
                      "currentValue": null,
                      "minimum": null,
                      "maximum": null,
                      "unit": "percent",
                      "description": null
                  },
                  {
                      "type": "integer",
                      "name": null,
                      "title": null,
                      "currentValue": null,
                      "minimum": null,
                      "maximum": null,
                      "unit": "milliseconds",
                      "description": null
                  }
              ]
          }
      }
      ]
    },
    {
      "uri": "/devices/851de9de-e6a6-4817-ac12-68a19f875e51",
      "id": "851de9de-e6a6-4817-ac12-68a19f875e51",
      "title": "My TV",
      "description": "A web connected TV",
      "category": null,
      "baseLink": null,
      "properties": [
          {
            "type": "boolean",
            "name": "Power",
            "title": null,
            "currentValue": 'Off',
            "minimum": null,
            "maximum": null,
            "unit": null,
            "description": "Whether the lamp is turned on"
        },
        {
            "type": "integer",
            "name": "Brightness",
            "title": null,
            "currentValue": 0,
            "minimum": null,
            "maximum": null,
            "unit": "percent",
            "description": "The level of light from 0-100"
        }
      ],
      "actions": [
          {
              "name": "Power On/Off",
              "title": null,
              "description": "Fade the lamp to a given level",
              "input": {
                  "type": "object",
                  "required": [
                      "brightness",
                      "duration"
                  ],
                  "properties": [
                      {
                          "type": "integer",
                          "name": null,
                          "title": null,
                          "currentValue": null,
                          "minimum": null,
                          "maximum": null,
                          "unit": "percent",
                          "description": null
                      },
                      {
                          "type": "integer",
                          "name": null,
                          "title": null,
                          "currentValue": null,
                          "minimum": null,
                          "maximum": null,
                          "unit": "milliseconds",
                          "description": null
                      }
                  ]
              }
          },
          {
            "name": "Change volume",
            "title": null,
            "description": "Fade the lamp to a given level",
            "input": {
                "type": "object",
                "required": [
                    "brightness",
                    "duration"
                ],
                "properties": [
                    {
                        "type": "integer",
                        "name": null,
                        "title": null,
                        "currentValue": null,
                        "minimum": null,
                        "maximum": null,
                        "unit": "percent",
                        "description": null
                    },
                    {
                        "type": "integer",
                        "name": null,
                        "title": null,
                        "currentValue": null,
                        "minimum": null,
                        "maximum": null,
                        "unit": "milliseconds",
                        "description": null
                    }
                ]
            }
        }
      ]
    }
  ]
