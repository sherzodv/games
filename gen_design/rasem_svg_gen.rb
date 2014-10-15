
require 'rasem'

class Hex
	def initialize
		@r = 20
		@h = @r * 0.87
	end

	def setr(r)
		@r = r
		@h = @r * 0.87
	end

	def r
		@r
	end

	def h
		@h
	end

	def calc(x, y)
		v =[[x, y - @r],
			[x + @h, y - @r / 2],
			[x + @h, y + @r / 2],
			[x, y + @r],
			[x - @h, y + @r / 2],
			[x - @h, y - @r / 2]]
	end
end

$resX = 400
$resY = 400
$s = 2

def gen
	hex = Hex.new

	$img = Rasem::SVGImage.new($resX, $resY) do
		shift = false
		y = $s + hex.r
		while y < $resY
			x = $s + (shift ? 2*hex.h + $s/2: hex.h);
			shift = !shift;

			while x < $resX do
				polygon *hex.calc(x, y), :stroke=>"grey", :fill=>"grey"

				x += $s + 2 * hex.h
			end
			y += $s*0.87 + 1.5 * hex.r
		end
	end
end

def save
	File.open("default.svg", "w") do |file|
		file.puts $img.output
	end
end

gen
save

