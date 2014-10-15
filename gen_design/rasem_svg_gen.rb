#!/usr/local/bin/ruby

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

	def calc(x, y)
		v =[[x, y - @radius],
			[x + @height, y - @radius / 2],
			[x + @height, y + @radius / 2],
			[x, y + @radius],
			[x - @height, y + @radius / 2],
			[x - @height, y - @radius / 2]]
	end
end

Cos30 = 0.87

# distance between two hexagons
$s = 2

# image resolution
$resX = 400
$resY = 400

# preferred hexagons number along X
$hexX = 10

# preferred hexagons number along Y
$hexY = 0

# border width
$borderX = 0
$borderY = 0

$hex = Hex.new

def recalc
	if $hexY == 0 then
		$borderX = $s
		$hex.setr( ($resX - $s*($hexX+1))/($hexX*2*Cos30) )
	else
		$borderY = $s
		$hex.setr( ($resY - $s*($hexY+1))/($hexY*2) )
	end
end

def gen
	$img = Rasem::SVGImage.new($resX, $resY) do
		shift = false
		y = $borderY + $hex.radius
		while y < $resY
			x = $borderX + (shift ? 2*$hex.height + $s/2: $hex.height);
			shift = !shift;

			while x < $resX do
				polygon *$hex.calc(x, y), :stroke=>"grey", :fill=>"grey"

				x += $s + 2 * $hex.height
			end
			y += $s*Cos30 + 1.5 * $hex.radius
		end
	end
end

def save
	File.open("default.svg", "w") do |file|
		file.puts $img.output
	end
end

recalc
gen
save

