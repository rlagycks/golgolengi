import { request } from '../../api/client';
import type { Challenge, ChallengeCategory, WearableData } from '../../types';

export interface ChallengeListData {
  challenges: Challenge[];
  wearableData: WearableData | null;
}

interface BackendMission {
  missionId: string;
  familyId: string;
  title: string;
  description: string;
  category: string;
  status: string;
  targetCount: number;
  currentValue?: number;
  completedCount?: number;
  totalFamilyCount?: number;
  unit: string;
  endDate: string;
}

function mapCategory(category?: string): Challenge['category'] {
  switch ((category ?? '').toLowerCase()) {
    case 'walk':
    case 'walking':
      return 'walk';
    case 'diet':
      return 'diet';
    case 'sleep':
      return 'sleep';
    case 'water':
    case 'hydration':
      return 'water';
    default:
      return 'walk';
  }
}

function mapUnit(unit?: string): string {
  switch ((unit ?? '').toLowerCase()) {
    case 'steps':
      return '보';
    case 'glasses':
      return '잔';
    case 'hours':
      return '시간';
    case 'servings':
      return '회';
    case 'minutes':
      return '분';
    default:
      return unit ?? '';
  }
}

function mapMission(m: BackendMission): Challenge {
  const targetValue = m.targetCount ?? 1;
  const currentValue = m.currentValue ?? 0;
  const isCompleted = (m.status ?? '').toLowerCase() === 'completed' || currentValue >= targetValue;

  return {
    id: m.missionId,
    title: m.title,
    description: m.description ?? '',
    category: mapCategory(m.category),
    targetValue,
    currentValue,
    unit: mapUnit(m.unit),
    status: isCompleted ? 'completed' : 'ongoing',
    isAiRecommended: false,
    isUrgent: false,
    completedCount: m.completedCount ?? 0,
    totalFamilyCount: m.totalFamilyCount ?? 1,
    dueDate: m.endDate ? new Date(m.endDate).toISOString() : new Date().toISOString(),
  };
}

export async function fetchChallenges(): Promise<ChallengeListData> {
  const missions = await request<BackendMission[]>('GET', '/missions');
  return { challenges: missions.map(mapMission), wearableData: null };
}

export async function checkInChallenge(
  challengeId: string,
  value: number,
): Promise<{ success: boolean; newValue: number }> {
  const mission = await request<BackendMission>('POST', '/mission-logs', { missionId: challengeId, value });
  return { success: true, newValue: mission.currentValue ?? value };
}

export async function postponeChallenge(_challengeId: string): Promise<void> {
  // 백엔드 미구현
}

export { ChallengeCategory };
