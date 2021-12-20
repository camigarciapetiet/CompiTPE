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
b@andor DW ?
a@andor DW ?
YAC db "YAC", 0 
overflow_error db "error de overflow", 0
div_zero db "error division por cero", 0
rec_mutua db "error recursion mutua", 0
@aux1 DW ?
@aux2 DW ?
@aux3 DW ?

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
andor:
MOV AX, 4
MOV a@andor, AX
MOV AX, 6
MOV b@andor, AX
MOV @aux1, 0
MOV @aux2, 0
MOV AX, 2
ADC AX, 2
JO OVERFLOW_ERROR
MOV AX, 2
ADC AX, 2
MOV @aux3, AX
MOV AX, a@andor
CMP AX, @aux3
JNE Label1
MOV @aux1, 1
Label1:
MOV AX, b@andor
CMP AX, 50
JAE Label2
MOV @aux2, 1
Label2:
MOV AX, @aux1
AND AX, @aux2
JZ Label3
invoke MessageBox, NULL, addr YAC, addr YAC, MB_OK
Label3: 
END andor
invoke MessageBox, NULL, addr FIN, NULL, MB_OK

