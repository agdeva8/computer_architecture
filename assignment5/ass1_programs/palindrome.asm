	.data
a:
	101
	.text
main:
	load %x0, $a, %x1
	load %x0, $a, %x2
	addi %x0, 0, %x3
loop:
	beq %x2, %x0, result
	divi %x2, 10, %x2
	muli %x3, 10, %x3
	add %x3, %x31, %x3
	jmp loop
result:
	beq %x3, %x1, ispalin
	subi %x0, 1, %x10
	end
ispalin:
	addi %x0, 1, %x10
	end
