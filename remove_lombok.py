import os
import re

def to_camel_case(snake_str):
    components = snake_str.split('_')
    return components[0] + ''.join(x.title() for x in components[1:])

def capitalize(s):
    return s[0].upper() + s[1:]

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    if 'lombok' not in content:
        return

    print(f"Processing {filepath}")
    
    # Remove lombok imports
    content = re.sub(r'import lombok\..*?;\n', '', content)
    
    has_data = '@Data' in content
    has_no_args = '@NoArgsConstructor' in content
    has_all_args = '@AllArgsConstructor' in content
    
    # Remove annotations
    content = re.sub(r'@Data\s*\n', '', content)
    content = re.sub(r'@NoArgsConstructor\s*\n', '', content)
    content = re.sub(r'@AllArgsConstructor\s*\n', '', content)

    # Extract fields
    # Match: private [Type] [name] [= default];
    field_pattern = re.compile(r'^\s*private\s+([\w<>,\s]+?)\s+(\w+)(?:\s*=\s*[^;]+)?\s*;', re.MULTILINE)
    fields = field_pattern.findall(content)
    
    class_name_match = re.search(r'public class (\w+)', content)
    if not class_name_match:
        return
    class_name = class_name_match.group(1)

    generated_code = "\n"

    if has_no_args or has_data:
        generated_code += f"    public {class_name}() {{}}\n\n"

    if has_all_args:
        args = ", ".join([f"{t.strip()} {n}" for t, n in fields])
        assignments = "\n".join([f"        this.{n} = {n};" for t, n in fields])
        generated_code += f"    public {class_name}({args}) {{\n{assignments}\n    }}\n\n"

    if has_data:
        for t, n in fields:
            t = t.strip()
            cap_n = capitalize(n)
            # Getter
            if t == 'boolean':
                generated_code += f"    public {t} is{cap_n}() {{ return this.{n}; }}\n"
            else:
                generated_code += f"    public {t} get{cap_n}() {{ return this.{n}; }}\n"
            # Setter
            generated_code += f"    public void set{cap_n}({t} {n}) {{ this.{n} = {n}; }}\n\n"

    # Insert before the last closing brace
    last_brace_index = content.rfind('}')
    if last_brace_index != -1:
        content = content[:last_brace_index] + generated_code + content[last_brace_index:]

    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)

def main():
    base_dir = r"c:\Users\darsh\OneDrive\Documents\Desktop\compliance-training-manager - Copy\backend\src\main\java"
    for root, dirs, files in os.walk(base_dir):
        for file in files:
            if file.endswith(".java"):
                process_file(os.path.join(root, file))

if __name__ == "__main__":
    main()
