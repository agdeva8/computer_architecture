	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
	addi %x0, 1, %x3
	load %x0, $n, %x8
loopmain:
	add %x0, %x0, %x4
	addi %x0, 1, %x5
loopsub:
	load %x4, $a, %x6
	load %x5, $a, %x7
	bgt %x6, %x7, noswap
	store %x7, $a, %x4
	store %x6, $a, %x5
noswap:
	addi %x4, 1, %x4
	addi %x5, 1, %x5
	beq %x5, %x8, checksorted
	jmp loopsub
checksorted:
	subi %x8, 1, %x8
	beq %x8, %x3, endl
	jmp loopmain
endl:
	end
