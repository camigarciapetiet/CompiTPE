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
diseno@precondicion DW ?
auxiliarfuncion@precondicion@funcion1 DW ?
termino db "termino", 0 
diseno@precondicion@funcion1 DW ?
overflow_error db "error de overflow", 0
div_zero db "error division por cero", 0
rec_mutua db "error recursion mutua", 0
@aux1 DW ?
@funcion1@precondicion DW 0

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
funcion1@precondicion:
MOV AX, diseno@precondicion@funcion1
CMP AX, 2
JAE Label1
invoke MessageBox, NULL, addr pasajeparametrocorrecto, addr pasajeparametrocorrecto, MB_OK
Label1: 
MOV AX, 5
MOV auxiliarfuncion@precondicion@funcion1, AX
MOV AX, 10
MOV @aux1, AX 
ret
Labelnull:
invoke MessageBox, NULL, addr ejecucionabortada, addr ejecucionabortada, MB_OK
JMP EXIT
precondicion:
;CHEQUEO RECURSION MUTUA
CMP @funcion1@precondicion, 1
JE ERROR_REC_MUTUA
MOV @funcion1@precondicion, 1
;END CHEQUEO RECURSION MUTUA
MOV AX,3
MOV diseno@precondicion@funcion1, AX
CALL funcion1@precondicion
MOV @funcion1@precondicion, 0
MOV AX, @aux1
MOV diseno@precondicion, AX
invoke MessageBox, NULL, addr termino, addr termino, MB_OK
END precondicion
invoke MessageBox, NULL, addr FIN, NULL, MB_OK

