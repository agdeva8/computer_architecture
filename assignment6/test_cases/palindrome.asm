.data
a:
	4567654
	.text
main:
	load %x0, $a, %x3	1
	sub %x7, %x7, %x7
loop:
	divi %x3, 10, %x4	3
	addi %x31, 0, %x30
	muli %x7, 10, %x7	5
	add %x7, %x30, %x7
	divi %x3, 10, %x3	7
	bgt %x3, %x0, loop
	load %x0, $a, %x5	9
	beq %x5, %x7, palindrome
	sub %x10, %x10, %x10	11
	subi %x10, 1, %x10
	end					13
palindrome:
	sub %x10, %x10, %x10	15
	addi %x10, 1, %x10
	end						17