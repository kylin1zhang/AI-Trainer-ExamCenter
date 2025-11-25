#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""简化版数据导入脚本 - 自动导入，无需交互"""

import json
import mysql.connector
import sys

# 数据库配置
DB_CONFIG = {
    'host': 'localhost',
    'user': 'root',
    'password': 'Asdf1234',
    'database': 'exam_center',
    'charset': 'utf8mb4'
}

BANK_ID = 1

def main():
    print("正在导入题目数据...")
    
    # 1. 读取 JSON
    with open('data.json', 'r', encoding='utf-8') as f:
        data = json.load(f)
    print(f"读取到 {len(data)} 条题目")
    
    # 2. 连接数据库
    conn = mysql.connector.connect(**DB_CONFIG)
    cursor = conn.cursor()
    print("数据库已连接")
    
    # 3. 清空现有数据
    cursor.execute(f"DELETE FROM tb_question WHERE bank_id = {BANK_ID}")
    conn.commit()
    print("已清空现有数据")
    
    # 4. 插入数据
    insert_sql = """
    INSERT INTO tb_question 
    (id, bank_id, type, question, option_a, option_b, option_c, option_d, option_e, answer, explanation, sequence_number)
    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    """
    
    success = 0
    for idx, item in enumerate(data, 1):
        try:
            cursor.execute(insert_sql, (
                item['id'], BANK_ID, item['type'], item['question'],
                item.get('option_A'), item.get('option_B'), item.get('option_C'),
                item.get('option_D'), item.get('option_E'),
                item['answer'], item.get('explanation'), idx
            ))
            success += 1
            if idx % 100 == 0:
                print(f"已导入 {idx}/{len(data)} 条...")
        except Exception as e:
            print(f"题目 {item['id']} 导入失败: {e}")
    
    # 5. 更新题库统计
    cursor.execute(f"UPDATE tb_question_bank SET question_count = {success} WHERE id = {BANK_ID}")
    conn.commit()
    
    print(f"\n导入完成！成功: {success} 条")
    
    cursor.close()
    conn.close()

if __name__ == '__main__':
    try:
        main()
    except Exception as e:
        print(f"错误: {e}")
        sys.exit(1)

