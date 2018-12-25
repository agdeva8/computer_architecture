.data
n:
	0
	1
	.text
main:
	load %x0, $n, %x3
	load %x0, 1, %x4
	add %x3, %x4, %x5
	sub %x6, %x6, %x6
	addi %x6, 10, %x6
	sub %x7, %x7, %x7
	addi %x7, 2, %x7
for:
	blt %x7, %x6, loop
	end
loop:
	add %x3, %x4, %x5
	store %x7, 0, %x5
	add %x0, %x4, %x3
	add %x0, %x5, %x4	
	addi %x7, 1, %x7
	jmp for