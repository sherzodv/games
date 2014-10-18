#!/usr/local/bin/ruby

# Запрашиваем Gem rasem для работы с svg
require 'rasem'

class Hex
	attr_accessor :radius
	attr_accessor :height

	def initialize
		@radius = 0
		@height = @radius * Cos30
	end

	def setr(r)
		@radius = r
		@height = @radius * Cos30
	end

	def draw(img, x, y)
		v =[[x, y - @radius],
			[x + @height, y - @radius / 2],
			[x + @height, y + @radius / 2],
			[x, y + @radius],
			[x - @height, y + @radius / 2],
			[x - @height, y - @radius / 2]]

		img.polygon *v, :stroke=>"grey", :fill=>"grey"
	end
end

Cos30 = 0.87

# distance between two hexagons
$s = 2
$orien = 'X'

# image resolution
$resX = 800
$resY = 400

# preferred hexagons number along X
$hexX = 20

# preferred hexagons number along Y
$hexY = 0

# border width
$borderX = 0
$borderY = 0

$filename = "default.svg"

$hex = Hex.new

def recalc
	if $orien == 'X' then
		$borderX = $s
		$borderY = $s

		$hex.setr ($resX - $s*($hexX+1)) / ($hexX*2*Cos30)

		# count number of hexes in column
		$hexY = (($resY - $s - 0.5*$hex.radius) / ($s + 1.5 * $hex.radius)).to_i

		$resY = $s*($hexY+1) + 1.5*($hex.radius)*$hexY + 0.5*($hex.radius)

		# counting size of border
	else
		$borderX = $s
		$borderY = $s

		$hex.setr ($resY - $s*($hexY+1)) / (1.5*$hexY + 0.5)

		# count number of hexes in row
		$hexX = (($resX - $s) / ($s + 2*$hex.height)).to_i

		$resX = $s*($hexX+1) + 2*$hex.height*$hexX;
	end
end

def gen
	$img = Rasem::SVGImage.new($resX, $resY)

	shift = false
	y = $borderY + $hex.radius
	$hexX -= 1

	$hexY.times do
		x = $borderX + (shift ? 2*$hex.height + $s/2: $hex.height);
		$hexX += (shift ? -1: +1);
		shift = !shift;

		$hexX.times do
			$hex.draw($img, x, y);
			# polygon *$hex.calc(x, y), :stroke=>"grey", :fill=>"grey"
			x += $s + 2 * $hex.height
		end

		y += $s*Cos30 + 1.5 * $hex.radius
	end
end

def save
	$img.close
	File.open($filename, "w") do |file|
		file.puts $img.output
	end
end

#___________________________PREEXECUTABLE_COMMANDS_____________________________

recalc

#__________________________________MAIN________________________________________

while true
	print "> "
	str = gets

	case str.split(' ')[0]
	when /^[Hh][Ee][Ll][Pp]/
		puts "|"
		puts "| gen"
		puts "| resolution <Xpixel> <Ypixel>"
		puts "|"
		puts "| X <n> - set number of hexes in a column, Y counts automatically"
		puts "| Y <n> - set number of hexes in a row, X counts automatically"
		puts "|"
		puts "| set_s <pixel>"
		puts "|"
		puts "| set_output_file <path>"
		puts "| wtf - show all parameters"
		puts "| toggle_orien"
		puts "|"
		puts "| exit"
		puts ""

	when "toggle_orien"
		$orien = ($orien == 'X' ? 'Y': 'X');

	when "gen"
		recalc
		gen
		save

	when "resolution"
		$resX, $resY = str.split(' ')[1].to_i, str.split(' ')[2].to_i

	when "set_s"
		$s = str.split(' ')[1].to_i

	when "set_output_file"
		$filename = str.split(' ')[1]

	when "X"
		$hexX = str.split(' ')[1].to_i
		recalc

	when "Y"
		$hexY = str.split(' ')[1].to_i
		recalc

	when /^[Ww][Tt][Ff]/
		puts "|"
		puts "| resolution: " + $resX.to_s + "x" + $resY.to_s
		puts "| hexolution: " + $hexX.to_s + "x" + $hexY.to_s
		puts "| dist between hexes: " + $s.to_s
		puts "| output file name: " + $filename
		puts "| orien: " + $orien
		puts ""

	when /^[Ee][Xx][Ii][Tt]/
		break
	end
end

