const express = require('express')
const app = express()
const port = 13905;

const axios = require('axios')

app.get('/:userId/*', async (req, res) => {
    const userId = req.params.userId
    const originalUrl = '/' + req.originalUrl.split('/').slice(2).join('/')
    switch (true) {
        case ((new RegExp('.*/no.*/pr.*/\\?.*')).test(originalUrl)):
            res.status(200).json(
                {
                    "code": "UPC-200",
                    "data": {
                        "notification_preference_list": [
                            {
                                "enable": false,
                                "notification_type": "APP",
                                "template_name": "wallet_chat"
                            }
                        ]
                    }
                }
            )
            break
        case ((new RegExp('.*/ma.*ce-limit')).test(originalUrl)):
            try {
                const url = 'https://' + req.hostname + originalUrl
                const config = {
                    headers: { ...req.headers }
                }
                const response = await axios.get(url, config)
                response.data['code'] = 'ABCDEFG-1234567'
                response.data['data']['new_data_key'] = 'new_data_value'
                response.data['new_key'] = 'new_value'
                res.status(response.status).json(response.data)
            } catch (error) {
                if (error.response) {
                    res.status(error.response.status).send(error.response.data)
                } else {
                    res.status(500).send('Internal mock-server error<br/>' + error)
                }
            }
            break
        default:
            try {
                const url = 'https://' + req.hostname + originalUrl
                const config = {
                    headers: { ...req.headers }
                }
                const response = await axios.get(url, config)
                res.status(response.status).send(response.data)
            } catch (error) {
                if (error.response) {
                    res.status(error.response.status).send(error.response.data)
                } else {
                    res.status(500).send('Internal mock-server error<br/>' + error)
                }
            }
            break
    }
})

app.listen(port, () => {
    console.log(`Express server running at http://localhost:${port}`);
})
