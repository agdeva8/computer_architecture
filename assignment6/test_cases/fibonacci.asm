.data
n:
	10
	.text
main:
	addi %x0, 0, %x3	1
	addi %x0, 1, %x4
	add %x3, %x4, %x5	3
	load %x0, $n, %x6
	addi %x0, 65535, %x7	5
	addi %x0, 0, %x8
	store %x3, 0, %x7	7
	subi %x7, 1, %x7
	addi %x8, 1, %x8	9
	store %x4, 0, %x7	
	subi %x7, 1, %x7	11
	addi %x8, 1, %x8
for:
	blt %x8, %x6, loop	13
	end
loop:
	add %x3, %x4, %x5	15
	store %x5, 0, %x7	
	subi %x7, 1, %x7	17
	addi %x8, 1, %x8
	add %x0, %x4, %x3	19
	add %x0, %x5, %x4
	jmp for				21