CMP AX, 5
JNE Label1
MOV AX, 4
MOV diseno@test, AX
Label1: 
END test
invoke MessageBox, NULL, addr FIN, NULL, MB_OK

