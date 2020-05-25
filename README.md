[![codecov](https://codecov.io/gh/Lonor/JWT-practice/branch/master/graph/badge.svg?token=M57NG2928G)](https://codecov.io/gh/Lonor/JWT-practice)

[![Build Status](https://travis-ci.com/Lonor/JWT-practice.svg?token=9mEAEtSyyh5dpJEP16Bx&branch=master)](https://travis-ci.com/Lonor/JWT-practice)

HTTP API：

地址：localhost:8080

登录：

GET /api/login?phone={phone}

登录后，获取 Token，将其放置于请求头 token 字段中，再尝试访问其他资源 API：

GET /api/test

