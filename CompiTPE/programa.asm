.MODEL small
.STACK 200h
.DATA
aux.programa DD ?
disenomenora2 db "disenomenora2", 0 
auxiliarfuncion2.programa.funcion1 DW ?
YACYetAnotherChain db "YACYetAnotherChain", 0 
diseno.programa DW ?
c.programa DD ?
diseno.programa.funcion1 DW ?
auxiliarfuncion.programa.funcion1 DW ?
b.programa DD ?
YAC db "YAC", 0 
a.programa DD ?
Cadenabienescrita db "Cadenabienescrita", 0 
recfuncion db "none", 0
@aux1 DD ?
@aux2 DD ?

.CODE
funcion1.programa:
MOV AX, diseno.programa.funcion1
CMP AX, 2
JL Label1
MOV auxiliarfuncion.programa.funcion1, 5
MOV BX, 2
CMP BX, diseno.programa.funcion1
JO OVERFLOW_ERROR
MOV AX, diseno.programa.funcion1
ADD AX, 2
MOV @aux1, AX
MOV @aux2, @aux1
ret
programa:
MOV diseno.programa, -999
MOV aux.programa, 2.33S5
invoke MessageBox, NULL, addr, Cadenabienescrita, addr Cadenabienescrita, MB_OK
MOV AX, diseno.programa
CMP AX, 5
JL Label2
invoke MessageBox, NULL, addr, YAC, addr YAC, MB_OK
MOV diseno.programa, 5
Label2: 
CMP recfuncion, "funcion1.programa" 
JE ERROR_REC_MUTUA
MOV recfuncion, "funcion1.programa" 
MOV diseno.programa.funcion1, diseno.programa
CALL funcion1.programa
MOV recfuncion, "empty" 
MOV diseno.programa, @aux2
END programa
invoke MessageBox, NULL, addr FIN, NULL, MB_OK
EXIT:
 invoke ExitProcess, 0
DIV_CERO:
 invoke MessageBox, NULL, addr CERO, NULL, MB_OK
 JMP EXIT
OVERFLOW_ERROR:
 invoke MessageBox, NULL, addr OVERFLOW_ERROR, NULL, MB_OK
 JMP EXIT
ERROR_REC_MUTUA:
 invoke MessageBox, NULL, addr ERROR_REC_MUTUA, NULL, MB_OK
 JMP EXIT

