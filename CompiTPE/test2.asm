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
termino db "termino", 0 
diseno@test2 DW ?
overflow_error db "error de overflow", 0
div_zero db "error division por cero", 0
rec_mutua db "error recursion mutua", 0
@aux1 DW ?

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
MOV AX, -32767
MOV diseno@test2, AX
MOV AX, -500
ADC AX, diseno@test2
JO OVERFLOW_ERROR
MOV AX, diseno@test2
ADC AX, -500
MOV @aux1, AX
MOV AX, @aux1
MOV diseno@test2, AX
invoke MessageBox, NULL, addr termino, addr termino, MB_OK
END test2
invoke MessageBox, NULL, addr FIN, NULL, MB_OK

