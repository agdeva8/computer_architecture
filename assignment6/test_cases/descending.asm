.data
a:
	40
	20
	50
	60
	80
	30
	10
	70
n:
	8
	.text
main:
	sub %x3, %x3, %x3	[9]
	sub %x4, %x4, %x4
	load %x0, $n, %x8
outerloop:
	blt %x3, %x8, innerloop	[12]
	end
	addi %x3, 1, %x4
innerloop:
	addi %x3, 1, %x4	[15]
innerloopz:
	blt %x4, %x8, swap	[16]
	addi %x3, 1, %x3
	jmp outerloop
swap:
	load %x3, $a, %x5	[19]
	load %x4, $a, %x6
	blt %x5, %x6, exchange
	addi %x4, 1, %x4
	jmp innerloopz
exchange:
	sub %x7, %x7, %x7	[24]
	add %x0, %x5, %x7
	store %x6, 0, %x3
	store %x7, 0, %x4
	addi %x4, 1, %x4
	jmp innerloopz
