--
-- xdabbcode - BBCode writer for XDA forums
--     http://forum.xda-developers.com/misc.php?do=bbcode
--
-- Copyright (C) 2015 James Mason < jmason888 ! gmail ! com >
--
-- Invoke with: pandoc -t sample.lua

-- Blocksep is used to separate block elements.
function Blocksep()
  return "\n\n"
end

-- This function is called once for the whole document. Parameters:
-- body, title, date are strings; authors is an array of strings;
-- variables is a table.  One could use some kind of templating
-- system here; this just gives you a simple standalone HTML file.
function Doc(body, title, authors, date, variables)
  return body .. '\n'
end

-- The functions that follow render corresponding pandoc elements.
-- s is always a string, attr is always a table of attributes, and
-- items is always an array of strings (the items in a list).
-- Comments indicate the types of other variables.

function Str(s)
  return s
end

function Space()
  return " "
end

function LineBreak()
  return "\n"
end

function Emph(s)
  return "[I]" .. s .. "[/I]"
end

function Strong(s)
  return "[B]" .. s .. "[/B]"
end

function Strikeout(s)
  return '[STRIKE]' .. s .. '[/STRIKE]'
end

function Link(s, src, tit)
  if s then
    return '[URL=' .. src .. ']' .. s .. '[/URL]'
  else
    return '[URL]' .. src .. '[/URL]'
  end
end

function Image(s, src, tit)
  return "[IMG]" .. src .. "[/IMG]"
end

function Code(s, attr)
  return '[FONT="Courier New"]' .. s .. "[/FONT]"
end

function Plain(s)
  return s
end

function Para(s)
  return s
end

-- lev is an integer, the header level.
function Header(level, s, attr)
  if level == 1 then
 	 return "[B][SIZE=+3]" .. s .. "[/SIZE][/B]"
  elseif level == 2 then
 	 return "[B][SIZE=+2]" .. s .. "[/SIZE][/B]"
  elseif level == 3 then
 	 return "[B][SIZE=+1]" .. s .. "[/SIZE][/B]"
  elseif level == 4 then
 	 return "[B][U]" .. s .. "[/U][/B]"
  else
 	 return "[B]" .. s .. "[/B]"
  end
end

function BlockQuote(s)
  return "[QUOTE]\n" .. s .. "\n[/QUOTE]"
end

function HorizontalRule()
  return "[HR][/HR]"
end

function CodeBlock(s, attr)
    return "[CODE]\n" .. s .. "\n[/CODE]"
end

function BulletList(items)
  local buffer = {}
  for _, item in ipairs(items) do
    table.insert(buffer, "[*]" .. item)
  end
  return "[LIST]\n" .. table.concat(buffer, "\n") .. "\n[/LIST]"
end

function OrderedList(items)
  local buffer = {}
  for _, item in ipairs(items) do
    table.insert(buffer, "[*]" .. item)
  end
  return "[LIST=1]\n" .. table.concat(buffer, "\n") .. "\n[/LIST]"
end

-- Convert pandoc alignment to something HTML can use.
-- align is AlignLeft, AlignRight, AlignCenter, or AlignDefault.
function html_align(align)
  if align == 'AlignLeft' then
    return 'left'
  elseif align == 'AlignRight' then
    return 'right'
  elseif align == 'AlignCenter' then
    return 'center'
  else
    return 'left'
  end
end

-- The following code will produce runtime warnings when you haven't defined
-- all of the functions you need for the custom writer, so it's useful
-- to include when you're working on a writer.
local meta = {}
meta.__index =
  function(_, key)
    io.stderr:write(string.format("WARNING: Undefined function '%s'\n",key))
    return function() return "" end
  end
setmetatable(_G, meta)
