.386
.MODEL  Flat,StdCall
OPTION  CaseMap:None
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.STACK 200h
.DATA
pasajeparametrocorrecto db "pasajeparametrocorrecto", 0 
ejecucionabortada db "ejecucionabortada", 0 
diseno@testcases@funcion1 DW ?
diseno@testcases DW ?
aux@testcases DD ?
auxiliarfuncion@testcases@funcion1 DW ?
a@testcases DD ?
c@testcases DD ?
auxiliarfuncion2@testcases@funcion1 DW ?
Cadenabienescrita db "Cadenabienescrita", 0 
retornofuncionfunciona db "retornofuncionfunciona", 0 
_2_33S5 DD 233000.0
YACYetAnotherChain db "YACYetAnotherChain", 0 
b@testcases DD ?
overflow_error db "error de overflow", 0
div_zero db "error division por cero", 0
rec_mutua db "error recursion mutua", 0
@aux1 DW ?
@funcion1@testcases DW 0

.CODE
EXIT:
invoke ExitProcess, 0
DIV_CERO:
invoke MessageBox, NULL, addr div_zero, NULL, MB_OK
JMP EXIT
OVERFLOW_ERROR:
invoke MessageBox, NULL, addr overflow_error, NULL, MB_OK
JMP EXIT
ERROR_REC_MUTUA:
invoke MessageBox, NULL, addr rec_mutua, NULL, MB_OK
JMP EXIT
funcion1@testcases:
MOV AX, diseno@testcases@funcion1
CMP AX, 2
JAE Label1
invoke MessageBox, NULL, addr pasajeparametrocorrecto, addr pasajeparametrocorrecto, MB_OK
Label1: 
MOV AX, 5
MOV auxiliarfuncion@testcases@funcion1, AX
MOV AX, 10
MOV @aux1, AX 
ret
Labelnull:
invoke MessageBox, NULL, addr ejecucionabortada, addr ejecucionabortada, MB_OK
JMP EXIT
testcases:
MOV AX, -999
MOV diseno@testcases, AX
FLD _2_33S5
FSTP aux@testcases
invoke MessageBox, NULL, addr Cadenabienescrita, addr Cadenabienescrita, MB_OK
;REPEAT
MOV AX, 0
MOV diseno@testcases, AX
Label2:
MOV AX, diseno@testcases
CMP AX, 3
JAE Label3
invoke MessageBox, NULL, addr YACYetAnotherChain, addr YACYetAnotherChain, MB_OK
;BREAK
JMP Label3
ADC diseno@testcases, 1
JMP Label2
Label3:
;CHEQUEO RECURSION MUTUA
CMP @funcion1@testcases, 1
JE ERROR_REC_MUTUA
MOV @funcion1@testcases, 1
;END CHEQUEO RECURSION MUTUA
MOV AX,1
MOV diseno@testcases@funcion1, AX
CALL funcion1@testcases
MOV @funcion1@testcases, 0
MOV AX, @aux1
MOV diseno@testcases, AX
invoke MessageBox, NULL, addr retornofuncionfunciona, addr retornofuncionfunciona, MB_OK
Labelnull: 
MOV EAX, funcion1@testcases
MOV a@testcases, EAX
;CHEQUEO RECURSION MUTUA
CMP @funcion1@testcases, 1
JE ERROR_REC_MUTUA
MOV @funcion1@testcases, 1
;END CHEQUEO RECURSION MUTUA
MOV AX,1
MOV diseno@testcases@funcion1, AX
CALL [a@testcases]
MOV @funcion1@testcases, 0
MOV AX, @aux1
MOV diseno@testcases, AX
END testcases
invoke MessageBox, NULL, addr FIN, NULL, MB_OK

