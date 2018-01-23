start "gbus" server_gbus.bat 
start "abus1" server_abus1.bat
start "abus2" server_abus2.bat
pause 
start "mgr_gbus" mgr_gbus.bat
start "mgr_abus1" mgr_abus1.bat
start "mgr_abus2" mgr_abus2.bat
pause
start "router_a1g" router_a1g.bat
start "router_a2g" router_a2g.bat
pasuse
start "a1g" abus1_a1g.bat
start "a2g" abus2_a2g.bat
start "a1f" abus1_a1f.bat
start "a2f" abus2_a2f.bat
start "gg" gbus_gg.bat
start "gf" gbus_gf.bat