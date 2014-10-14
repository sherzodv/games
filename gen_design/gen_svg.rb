
$screen_width = 400
$screen_height = 400
$c = 'v'
$n = 10
$s = 1
$r = ($screen_height - $s * ($n + 1))/($n * 1.73)
$h = 0.87 * $r
$filename = "default.svg"


def draw_hex(h, r, x, y)
	v = [
		[x, y - r],
		[x + h, y - r/2],
		[x + h, y + r/2],
		[x, y+r],
		[x - h, y + r/2],
		[x - h, y - r/2]
	]
end

def gen
	file = File.open($filename, "w");
	file.puts "<?xml version=\"1.0\" standalone=\"no\"?>"
	file.puts "<svg width=\"#{$screen_width}\" height=\"#{$screen_height}\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">"

		shift = false
		$y = $s + $r
		while $y < $screen_height
			$x = $s + (shift ? 2*$h + $s/2 : $h);
			shift = !shift;
			while $x < $screen_width do
				file.puts "<polygon points=\"#{draw_hex($h, $r, $x, $y).join(' ')}\" style=\"fill:grey\" fill=\"transparent\" stroke-width=\"0\"/>"

				$x += $s + 2 * $h
			end
			$y += $s + 1.5*$r
		end
	file.puts "</svg>"
	file.close
end

while true
	print "> "
	str = gets

	case str.split(' ')[0]
	when /^[Hh][Ee][Ll][Pp]/
		puts "| Hi man!"
		puts "| available commands:"
		puts "| gen"
		puts "| set_width <pixel>"
		puts "| set_height <pixel>"
		puts "| set_s <pixel>"
		puts "| set_output_file <path>"
		puts "| toggle_orien - toggles orientation"
		puts "| set_hexn <n> - set number of hexes in a row or column"
		puts "| wtf - show all parameters"
		puts "| exit"
		puts ""
	when "gen"
		gen
	when "set_width"
		$screen_width = str.split(' ')[1].to_i
	when "set_height"
		$screen_height = str.split(' ')[1].to_i
	when "set_s"
		$s = str.split(' ')[1].to_i
	when "set_output_file"
		$filename = str.split(' ')[1]
	when "toggle_orien"
		$c = str.split(' ')[1]
		if $c == 'v' then $c = 'h' else $c = 'v' end
		case $c
		when 'v'
			$r = ($screen_height - $s * ($n + 1))/($n * 1.73)
		when 'h'
			$r = (2 * $screen_width - $s * ($n + 1) * 1.73) / (4 + 3 * $n);
		end
		$h = 0.87 * $r
	when "set_hexn"
		$n = str.split(' ')[1].to_i
	when "wtf"
		puts "| image size: " + $screen_width.to_s + "x" + $screen_height.to_s
		puts "| dist between hexes: " + $s.to_s
		puts "| orientation: " + $c
		puts "| hex number: " + $n.to_s
		puts "| output file name: " + $filename
		puts ""
	when /^[Ee][Xx][Ii][Tt]/
		break
	end
end

