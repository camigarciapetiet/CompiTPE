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
aux@programa DD ?
disenomenora2 db "disenomenora2", 0 
auxiliarfuncion2@programa@funcion1 DW ?
_2_33S5 DD 233000.0
YACYetAnotherChain db "YACYetAnotherChain", 0 
diseno@programa DW ?
c@programa DD ?
diseno@programa@funcion1 DW ?
auxiliarfuncion@programa@funcion1 DW ?
b@programa DD ?
YAC db "YAC", 0 
a@programa DD ?
Cadenabienescrita db "Cadenabienescrita", 0 
overflow_error db "error de overflow", 0
div_zero db "error division por cero", 0
rec_mutua db "error recursion mutua", 0
@aux1 DW ?
@aux2 DW ?
@funcion1@programa DW 0

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
funcion1@programa:
MOV AX, diseno@programa@funcion1
CMP AX, 2
JAE Label1
MOV AX, 5
MOV auxiliarfuncion@programa@funcion1, AX
MOV BX, 2
CMP BX, diseno@programa@funcion1
JO OVERFLOW_ERROR
MOV AX, diseno@programa@funcion1
ADD AX, 2
MOV @aux1, AX
MOV AX, @aux1
MOV @aux2, AX 
ret
Label1:
invoke MessageBox, NULL, addr disenomenora2, addr disenomenora2, MB_OK
JMP EXIT
programa:
MOV AX, -999
MOV diseno@programa, AX
FLD _2_33S5
FSTP aux@programa
invoke MessageBox, NULL, addr Cadenabienescrita, addr Cadenabienescrita, MB_OK
;REPEAT
MOV AX, 0
MOV diseno@programa, AX
Label2:
MOV AX, diseno@programa
CMP AX, 5
JAE Label3
invoke MessageBox, NULL, addr YACYetAnotherChain, addr YACYetAnotherChain, MB_OK
JMP Labelnull
ADD diseno@programa, 1
JMP Label2
Label3:
MOV AX, diseno@programa
CMP AX, 5
JAE Label4
invoke MessageBox, NULL, addr YAC, addr YAC, MB_OK
MOV AX, 5
MOV diseno@programa, AX
Label4: 
;CHEQUEO RECURSION MUTUA
CMP @funcion1@programa, 1
JE ERROR_REC_MUTUA
MOV @funcion1@programa, 1
;END CHEQUEO RECURSION MUTUA
MOV AX,diseno@programa
MOV diseno@programa@funcion1, AX
CALL funcion1@programa
MOV @funcion1@programa, 0
MOV AX, @aux2
MOV diseno@programa, AX
END programa
invoke MessageBox, NULL, addr FIN, NULL, MB_OK

