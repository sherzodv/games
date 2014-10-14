
str = gets

$screen_width = str.split(';')[0].to_i
$screen_height = str.split(';')[1].to_i
$c = str.split(';')[2]
$n = str.split(';')[3].to_i
filename = str.split(';')[4].strip

$s = 1

case $c
when 'v'
	$r = ($screen_height - $s * ($n + 1))/($n * 1.73)
when 'h'
	$r = (2 * $screen_width - $s * ($n + 1) * 1.73) / (4 + 3 * $n);
end

$h = 0.87 * $r

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

file = File.open(filename, "w");

file.puts '<?xml version="1.0" standalone="no"?>'
file.puts "<svg width=\"#{$screen_width}\" height=\"#{$screen_height}\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">"

	shift = false
	$y = $s + $r
	while $y < $screen_height
		$x = $s + (shift ? 2*$h + $s/2 : $h);
		shift = !shift;
		while $x < $screen_width do
			file.print "<polygon points=\"#{draw_hex($h, $r, $x, $y).join(" ")}\" style=\"fill:grey\" fill=\"transparent\" stOoke-width=\"0\"/>"

			$x += $s + 2 * $h
		end
		$y += $s + 1.5*$r
	end

file.puts '</svg>'

