#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
数据导入脚本
将 data.json 中的题目数据导入到 MySQL 数据库

使用方法：
1. 安装依赖：pip install mysql-connector-python
2. 修改数据库连接配置
3. 运行脚本：python scripts/import_data.py

作者：上海智学无界教育科技有限公司
日期：2025-11-24
"""

import json
import mysql.connector
from mysql.connector import Error
import os
import sys

# 数据库连接配置
DB_CONFIG = {
    'host': 'localhost',
    'user': 'root',
    'password': 'Asdf1234',  # 请修改为您的数据库密码
    'database': 'exam_center',
    'charset': 'utf8mb4'
}

# 题库ID（人工智能训练师三级）
BANK_ID = 1

def read_data_json(file_path='data.json'):
    """读取 data.json 文件"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            data = json.load(f)
        print(f"[OK] 成功读取 data.json，共 {len(data)} 条题目")
        return data
    except FileNotFoundError:
        print(f"[ERROR] 错误：找不到文件 {file_path}")
        sys.exit(1)
    except json.JSONDecodeError as e:
        print(f"[ERROR] 错误：JSON 解析失败 - {e}")
        sys.exit(1)

def connect_database():
    """连接数据库"""
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        if conn.is_connected():
            print(f"✅ 成功连接到数据库 {DB_CONFIG['database']}")
            return conn
    except Error as e:
        print(f"❌ 数据库连接失败：{e}")
        sys.exit(1)

def check_table_exists(cursor):
    """检查表是否存在"""
    cursor.execute("""
        SELECT COUNT(*) 
        FROM information_schema.tables 
        WHERE table_schema = %s 
        AND table_name = 'tb_question'
    """, (DB_CONFIG['database'],))
    
    result = cursor.fetchone()
    if result[0] == 0:
        print("❌ 错误：tb_question 表不存在，请先执行建表脚本")
        sys.exit(1)
    
    print("✅ 表 tb_question 存在")

def clear_existing_data(cursor):
    """清空现有数据（可选）"""
    response = input("⚠️  是否清空现有题目数据？(y/N): ").strip().lower()
    if response == 'y':
        cursor.execute("DELETE FROM tb_question WHERE bank_id = %s", (BANK_ID,))
        print(f"✅ 已清空题库 {BANK_ID} 的现有数据")
    else:
        print("⏭️  跳过清空数据")

def import_questions(cursor, data):
    """批量导入题目"""
    sql = """
    INSERT INTO tb_question (
        id, bank_id, type, question, 
        option_a, option_b, option_c, option_d, option_e,
        answer, explanation, sequence_number
    ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    ON DUPLICATE KEY UPDATE
        type = VALUES(type),
        question = VALUES(question),
        option_a = VALUES(option_a),
        option_b = VALUES(option_b),
        option_c = VALUES(option_c),
        option_d = VALUES(option_d),
        option_e = VALUES(option_e),
        answer = VALUES(answer),
        explanation = VALUES(explanation)
    """
    
    success_count = 0
    error_count = 0
    
    print("\n开始导入题目...")
    
    for i, item in enumerate(data, 1):
        try:
            values = (
                item['id'],
                BANK_ID,
                item['type'],
                item['question'],
                item.get('option_A'),
                item.get('option_B'),
                item.get('option_C'),
                item.get('option_D'),
                item.get('option_E'),
                item['answer'],
                item['explanation'],
                item['id']  # sequence_number 使用 id
            )
            cursor.execute(sql, values)
            success_count += 1
            
            # 每 100 条显示一次进度
            if i % 100 == 0:
                print(f"进度：{i}/{len(data)} ({i/len(data)*100:.1f}%)")
                
        except Error as e:
            error_count += 1
            print(f"❌ 题目 ID {item['id']} 导入失败：{e}")
    
    print(f"\n✅ 导入完成！成功：{success_count} 条，失败：{error_count} 条")
    return success_count, error_count

def analyze_data(data):
    """分析数据统计"""
    type_count = {}
    for item in data:
        q_type = item['type']
        type_count[q_type] = type_count.get(q_type, 0) + 1
    
    print("\n📊 数据统计：")
    print(f"   总题目数：{len(data)}")
    print(f"   判断题（judge）：{type_count.get('judge', 0)} 题")
    print(f"   单选题（single）：{type_count.get('single', 0)} 题")
    print(f"   多选题（multiple）：{type_count.get('multiple', 0)} 题")
    
    return type_count

def verify_import(cursor, expected_count):
    """验证导入结果"""
    print("\n🔍 验证导入结果...")
    
    # 统计总数
    cursor.execute("SELECT COUNT(*) FROM tb_question WHERE bank_id = %s", (BANK_ID,))
    actual_count = cursor.fetchone()[0]
    
    # 统计各题型数量
    cursor.execute("""
        SELECT type, COUNT(*) as count 
        FROM tb_question 
        WHERE bank_id = %s 
        GROUP BY type
    """, (BANK_ID,))
    
    type_counts = {}
    for row in cursor.fetchall():
        type_counts[row[0]] = row[1]
    
    print(f"   数据库中题目总数：{actual_count}")
    print(f"   判断题：{type_counts.get('judge', 0)} 题")
    print(f"   单选题：{type_counts.get('single', 0)} 题")
    print(f"   多选题：{type_counts.get('multiple', 0)} 题")
    
    if actual_count == expected_count:
        print("✅ 验证通过：数据完整")
    else:
        print(f"⚠️  警告：预期 {expected_count} 条，实际 {actual_count} 条")

def update_question_bank(cursor):
    """更新题库表的题目总数"""
    cursor.execute("""
        UPDATE tb_question_bank 
        SET question_count = (
            SELECT COUNT(*) 
            FROM tb_question 
            WHERE bank_id = %s
        )
        WHERE id = %s
    """, (BANK_ID, BANK_ID))
    print("✅ 已更新题库题目总数")

def main():
    """主函数"""
    print("=" * 60)
    print("  AI Trainer Exam Center - 数据导入工具")
    print("  上海智学无界教育科技有限公司")
    print("=" * 60)
    
    # 1. 读取 data.json
    data = read_data_json()
    
    # 2. 分析数据
    type_count = analyze_data(data)
    
    # 3. 连接数据库
    conn = connect_database()
    cursor = conn.cursor()
    
    try:
        # 4. 检查表是否存在
        check_table_exists(cursor)
        
        # 5. 清空现有数据（可选）
        clear_existing_data(cursor)
        
        # 6. 导入题目
        success_count, error_count = import_questions(cursor, data)
        
        # 7. 提交事务
        conn.commit()
        print("✅ 数据已提交到数据库")
        
        # 8. 验证导入结果
        verify_import(cursor, len(data))
        
        # 9. 更新题库表
        update_question_bank(cursor)
        conn.commit()
        
        print("\n" + "=" * 60)
        print("  🎉 数据导入成功完成！")
        print("=" * 60)
        
    except Error as e:
        conn.rollback()
        print(f"\n❌ 导入过程中发生错误：{e}")
        print("⚠️  已回滚所有更改")
        sys.exit(1)
        
    finally:
        cursor.close()
        conn.close()
        print("\n✅ 数据库连接已关闭")

if __name__ == '__main__':
    main()


