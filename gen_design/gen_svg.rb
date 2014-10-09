
s = nil
s = gets

rO = s.split(' ')[0].to_i;
filename = s.split(' ')[1];

ro = 0.87 * rO;

a = [
	[ro, 0],
	[2*ro, rO/2],
	[2*ro, 3*rO/2],
	[ro, 2*rO],
	[0, 3*rO/2],
	[0, rO/2],
]

file = File.open(filename, "w");
file.puts <<ENDSVG
<?xml version="1.0" standalone="no"?>
<svg width="256" height="256" version="1.1" xmlns="http://www.w3.org/2000/svg">
  <polygon points="#{a.join(" ")}" style="fill:grey" fill="transparent" stroke-width="0"/>
</svg>
ENDSVG

