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
YAC db "YAC", 0 
diseno@test2 DW ?
overflow_error db "error de overflow", 0
div_zero db "error division por cero", 0
rec_mutua db "error recursion mutua", 0
@aux1 DW ?
@aux2 DW ?
@aux3 DW ?
@aux4 DW ?

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
test2:
MOV AX, 2
MOV diseno@test2, AX
MOV BX, 3
CMP BX, 0
JE DIV_CERO
MOV @aux1, 3
MOV AX, 5
IDIV @aux1
MOV @aux2, AX
MOV AX, @aux2
IMUL AX, 2
MOV @aux3, AX
MOV BX, @aux3
CMP BX, diseno@test2
JO OVERFLOW_ERROR
MOV AX, diseno@test2
ADD AX, @aux3
MOV @aux4, AX
MOV AX, @aux4
CMP AX, 5
JAE Label1
invoke MessageBox, NULL, addr YAC, addr YAC, MB_OK
Label1: 
END test2
invoke MessageBox, NULL, addr FIN, NULL, MB_OK

