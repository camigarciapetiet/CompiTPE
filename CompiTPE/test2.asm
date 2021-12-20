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
aux@test2 DD ?
menoroiguala6 db "menoroiguala6", 0 
funcionamenor db "funcionamenor", 0 
_0_0 DD 0.0
_1_0 DD 1.0
_2_0 DD 2.0
_3_0 DD 3.0
_5_0 DD 5.0
_6_0 DD 6.0
termino db "termino", 0 
funcionaigual db "funcionaigual", 0 
diseno@test2 DD ?
overflow_error db "error de overflow", 0
div_zero db "error division por cero", 0
rec_mutua db "error recursion mutua", 0
@aux1 DD ?
@aux2 DD ?
@aux3 DD ?
@aux4 DD ?

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
;CASO DOS CONSTANTES
FLD _1_0
FADD _1_0
FSTP @aux1
FLD @aux1
FSTP diseno@test2
invoke MessageBox, NULL, addr funcionamenor, addr funcionamenor, MB_OK
Labelnull: 
FLD _2_0
FSTP diseno@test2
FLD _2_0
FSTP aux@test2
FLDZ 
FCOM aux@test2
FSTSW AX
SAHF
JE DIV_CERO
FLD diseno@test2
FDIV aux@test2
FSTP @aux2
FLD @aux2
FSTP diseno@test2
invoke MessageBox, NULL, addr funcionaigual, addr funcionaigual, MB_OK
Labelnull: 
FLD _2_0
FMUL _3_0
FSTP @aux3
FLD @aux3
FSTP diseno@test2
invoke MessageBox, NULL, addr menoroiguala6, addr menoroiguala6, MB_OK
Labelnull: 
FLDZ 
FCOM _0_0
FSTSW AX
SAHF
JE DIV_CERO
FLD _5_0
FDIV _0_0
FSTP @aux4
FLD @aux4
FSTP diseno@test2
invoke MessageBox, NULL, addr termino, addr termino, MB_OK
END test2
invoke MessageBox, NULL, addr FIN, NULL, MB_OK

