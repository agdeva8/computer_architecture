	.data
n:
	10
	.text
main:
	addi %x0, 1, %x1
	addi %x0, 0, %x2
	addi %x0, 1, %x3
	load %x0, $n, %x4
	addi %x0, 65535, %x6
	sub %x6, %x4, %x7
	beq %x6, %x7, endl
	store %x2, 0, %x6
	subi %x6, 1, %x6
	beq %x6, %x7, endl
	store %x3, 0, %x6
	subi %x6, 1, %x6
	beq %x6, %x7, endl
loop:
	add %x2, %x3, %x5
	store %x5, 0, %x6
	subi %x6, 1, %x6
	beq %x6, %x7, endl
	addi %x3, 0, %x2
	addi %x5, 0, %x3
	jmp loop
endl:
	end
