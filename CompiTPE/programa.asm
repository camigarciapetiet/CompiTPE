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
diseno@programa@funcion1@funcadentro DW ?
funcion1adentro db "funcion1adentro", 0 
hola db "hola", 0 
aux@programa@funcion2 DW ?
diseno@programa DW ?
diseno@programa@funcion1 DW ?
auxiliarfuncion@programa@funcion1 DW ?
parametro@programa@funcion2 DW ?
funcioneshermanas db "funcioneshermanas", 0 
parametroperfecto db "parametroperfecto", 0 
auxfuncadentro@programa@funcion1@funcadentro DW ?
overflow_error db "error de overflow", 0
div_zero db "error division por cero", 0
rec_mutua db "error recursion mutua", 0
@aux1 DW ?
@aux2 DW ?
@aux3 DW ?
@funcion2@programa DW 0
@funcadentro@programa@funcion1 DW 0
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
funcion2@programa:
invoke MessageBox, NULL, addr funcioneshermanas, addr funcioneshermanas, MB_OK
MOV AX, parametro@programa@funcion2
MOV @aux1, AX 
ret
funcadentro@programa@funcion1:
invoke MessageBox, NULL, addr funcion1adentro, addr funcion1adentro, MB_OK
MOV AX, 3
MOV diseno@programa@funcion1@funcadentro, AX
MOV AX, 5
MOV @aux2, AX 
ret
funcion1@programa:
;CHEQUEO RECURSION MUTUA
CMP @funcadentro@programa@funcion1, 1
JE ERROR_REC_MUTUA
MOV @funcadentro@programa@funcion1, 1
;END CHEQUEO RECURSION MUTUA
MOV AX,diseno@programa@funcion1
MOV diseno@programa@funcion1@funcadentro, AX
CALL funcadentro@programa@funcion1
MOV @funcion2@programa, 0
MOV @funcadentro@programa@funcion1, 0
MOV @funcion1@programa, 0
MOV AX, @aux2
MOV diseno@programa@funcion1, AX
;CHEQUEO RECURSION MUTUA
CMP @funcion1@programa, 1
JE ERROR_REC_MUTUA
MOV @funcion1@programa, 1
;END CHEQUEO RECURSION MUTUA
MOV AX,5
MOV diseno@programa@funcion1, AX
CALL funcion1@programa
MOV @funcion2@programa, 0
MOV @funcadentro@programa@funcion1, 0
MOV @funcion1@programa, 0
MOV AX, @aux2
MOV diseno@programa@funcion1, AX
MOV AX, diseno@programa@funcion1
MOV @aux3, AX 
ret
programa:
invoke MessageBox, NULL, addr hola, addr hola, MB_OK
;CHEQUEO RECURSION MUTUA
CMP @funcion1@programa, 1
JE ERROR_REC_MUTUA
MOV @funcion1@programa, 1
;END CHEQUEO RECURSION MUTUA
MOV AX,diseno@programa
MOV diseno@programa@funcion1, AX
CALL funcion1@programa
MOV @funcion2@programa, 0
MOV @funcadentro@programa@funcion1, 0
MOV @funcion1@programa, 0
MOV AX, @aux3
MOV diseno@programa, AX
MOV AX, diseno@programa
CMP AX, 5
JNE Label1
invoke MessageBox, NULL, addr parametroperfecto, addr parametroperfecto, MB_OK
Label1: 
END programa
invoke MessageBox, NULL, addr FIN, NULL, MB_OK

