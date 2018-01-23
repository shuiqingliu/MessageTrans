start server.bat
ping /n 2 127.1>nul
start manager.bat
ping /n 1 127.1>nul
start  op_server.bat
ping /n 1 127.1>nul
start  op_clients.bat