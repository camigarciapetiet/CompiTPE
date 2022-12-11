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
cadena1 db "cadena1", 0 
a@programa@f2 DW ?
cadenaprecondicionf2 db "cadenaprecondicionf2", 0 
b@programa DW ?
aesiguala2 db "aesiguala2", 0 
aux1@programa@f2 DW ?
a@programa@f1 DW ?
a@programa DW ?
aux1@programa@f1 DW ?
overflow_error db "error de overflow", 0
div_zero db "error division por cero", 0
rec_mutua db "error recursion mutua", 0
@auxpre DW ?
@aux1 DW ?
@aux2 DW ?
@aux3 DW ?
@f2@programa DW 0
@f1@programa DW 0

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
f2@programa:
MOV AX, a@programa@f2
CMP AX, 1
JAE Label1
MOV AX, a@programa@f2
ADC AX, 5
JO OVERFLOW_ERROR
MOV AX, 5
ADC AX, a@programa@f2
MOV @aux1, AX
MOV AX, @aux1
MOV aux1@programa@f2, AX
MOV AX, aux1@programa@f2
MOV @aux2, AX 
MOV @auxpre, 1 
ret
Label1:
invoke MessageBox, NULL, addr cadenaprecondicionf2, addr cadenaprecondicionf2, MB_OK
MOV @auxpre, 0
ret
f1@programa:
MOV AX, a@programa@f1
CMP AX, 1
JAE Label2
;CHEQUEO RECURSION MUTUA
CMP @f2@programa, 1
JE ERROR_REC_MUTUA
MOV @f2@programa, 1
;END CHEQUEO RECURSION MUTUA
MOV AX,3
MOV a@programa@f2, AX
CALL f2@programa
MOV @f2@programa, 0
MOV @f1@programa, 0
CMP @auxpre, 0 
JE Label3
MOV AX, @aux2
MOV aux1@programa@f1, AX
JMP Label4
Label3:
MOV AX, 10
MOV aux1@programa@f1, AX
Label4:
MOV AX, aux1@programa@f1
MOV @aux3, AX 
MOV @auxpre, 1 
ret
Label2:
invoke MessageBox, NULL, addr cadena1, addr cadena1, MB_OK
MOV @auxpre, 0
ret
programa:
MOV AX, 2
MOV a@programa, AX
MOV AX, a@programa
CMP AX, 2
JNE Label5
invoke MessageBox, NULL, addr aesiguala2, addr aesiguala2, MB_OK
Label5: 
MOV AX, 6
MOV b@programa, AX
;CHEQUEO RECURSION MUTUA
CMP @f1@programa, 1
JE ERROR_REC_MUTUA
MOV @f1@programa, 1
;END CHEQUEO RECURSION MUTUA
MOV AX,0
MOV a@programa@f1, AX
CALL f1@programa
MOV @f2@programa, 0
MOV @f1@programa, 0
CMP @auxpre, 0 
JE Label6
MOV AX, @aux3
MOV b@programa, AX
JMP Label7
Label6:
MOV AX, 10
MOV b@programa, AX
Label7:
END programa
invoke MessageBox, NULL, addr FIN, NULL, MB_OK

