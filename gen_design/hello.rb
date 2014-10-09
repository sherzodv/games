
str = nil

def str.hello( )
	"hello, world!"
end

puts( str.hello( ) )
puts( if 5 > 3 then "One" else "Another" end )
puts( var = 5 )
puts "================================================="

a = [1, 'hi', 3.14, 1, 2, [4, 5] * 3]
puts a
puts "================================================="

a.flatten.uniq
puts ( if a.index(6) == nil then "nil" else "something" end )
puts "================================================="

a.flatten!
puts a.index(4)
puts "================================================="

