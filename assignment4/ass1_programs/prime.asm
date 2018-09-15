	.data
a:
	289
	.text
main:
	addi %x0, 2, %x1
	load %x0, $a, %x2
	divi %x2, 2, %x3
	blt %x2, %x1, endl
loop:
	bgt %x1, %x3, isprime
	addi %x0, 0, %x31
	div %x2, %x1, %x4
	beq %x31, %x0, notprime
	addi %x1, 1, %x1
	jmp loop
isprime:
	addi %x0, 1, %x10
	end
notprime:
	subi %x0, 1, %x10
	end
endl:
	end
